package com.uitopic.restock.platform.iam.application.internal.commandservices;

import com.uitopic.restock.platform.iam.application.internal.outboundservices.hashing.HashingService;
import com.uitopic.restock.platform.iam.domain.model.aggregates.User;
import com.uitopic.restock.platform.iam.domain.model.commands.SignInCommand;
import com.uitopic.restock.platform.iam.domain.model.commands.SignUpCommand;
import com.uitopic.restock.platform.iam.domain.model.valueobjects.Email;
import com.uitopic.restock.platform.iam.domain.model.entities.Role;
import com.uitopic.restock.platform.iam.domain.model.valueobjects.RoleType;
import com.uitopic.restock.platform.iam.domain.repositories.UserRepository;
import com.uitopic.restock.platform.iam.domain.services.UserCommandService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * Implementation of {@link UserCommandService} for handling user-related actions
 * such as registering a new user (SignUp) and authenticating existing users (SignIn).
 */
@Service
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final HashingService hashingService;

    public UserCommandServiceImpl(UserRepository userRepository, HashingService hashingService) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
    }

    /**
     * Handles the sign-in command to authenticate a user.
     * Looks up the user by their email address and validates the password hash.
     *
     * @param command the sign-in command containing the credentials
     * @return an {@link Optional} containing the authenticated user if credentials are valid,
     *         or empty if user is not found or password doesn't match
     */
    @Override
    public Optional<User> handle(SignInCommand command) {
        return userRepository.findByEmailValue(command.email())
                .filter(user -> hashingService.matches(command.password(), user.getPasswordHash()));
    }

    /**
     * Handles the sign-up command to register a new user in the system.
     * Validates that the email is unique, format is correct, and role type is recognized.
     * Encodes the user password using a hashing service.
     *
     * @param command the sign-up command containing registration details
     * @return the newly created and saved {@link User} entity
     * @throws ResponseStatusException with status 409 Conflict if email is already in use,
     *                                 or 400 Bad Request if validation of email or role fails
     */
    @Override
    public User handle(SignUpCommand command) {
        if (userRepository.existsByEmailValue(command.email()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered: " + command.email());

        Email email;
        try {
            email = new Email(command.email());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        RoleType roleType;
        try {
            roleType = RoleType.valueOf(command.role().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown role: " + command.role());
        }

        String passwordHash = hashingService.encode(command.password());
        User user = new User(email, passwordHash, new Role(roleType), null);
        return userRepository.save(user);
    }
}
