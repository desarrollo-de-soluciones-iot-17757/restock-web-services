package com.uitopic.restock.platform.iam.application.internal.commandservices;

import com.uitopic.restock.platform.iam.application.internal.outboundservices.hashing.HashingService;
import com.uitopic.restock.platform.iam.application.internal.outboundservices.tokens.TokenService;
import com.uitopic.restock.platform.iam.domain.model.aggregates.User;
import com.uitopic.restock.platform.iam.domain.model.commands.SignInCommand;
import com.uitopic.restock.platform.iam.domain.model.commands.SignUpCommand;
import com.uitopic.restock.platform.iam.domain.model.entities.Role;
import com.uitopic.restock.platform.iam.domain.model.valueobjects.RoleType;
import com.uitopic.restock.platform.iam.domain.repositories.UserRepository;
import com.uitopic.restock.platform.iam.domain.services.UserCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * Implementation of {@link UserCommandService} for handling user-related actions
 * such as registering a new user (sign-up) and authenticating existing users (sign-in).
 *
 * <p>{@link TokenService} is injected here so that token generation happens inside
 * the application layer, not in the interfaces layer.
 */
@Slf4j
@Service
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;

    public UserCommandServiceImpl(UserRepository userRepository,
                                  HashingService hashingService,
                                  TokenService tokenService) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
    }

    /**
     * Handles the sign-in command to authenticate a user.
     * Looks up the user by their email address and validates the password hash.
     * Returns the authenticated user wrapped together with a generated JWT token
     * as a String array: [userId, token].
     *
     * @param command the sign-in command containing the credentials
     * @return an {@link Optional} containing a String array [userId, token] if
     *         credentials are valid, or empty if authentication fails
     */
    @Override
    public Optional<String[]> handle(SignInCommand command) {
        log.info("Sign-in attempt for email: {}", command.email());
        return userRepository.findByEmail(command.email())
                .filter(user -> {
                    boolean matches = hashingService.matches(command.password(), user.getPasswordHash());
                    if (!matches) log.warn("Invalid password for email: {}", command.email());
                    return matches;
                })
                .map(user -> {
                    String token = tokenService.generateToken(user);
                    log.info("Sign-in successful for user ID: {}", user.getId());
                    return new String[]{user.getId(), user.getEmail(), user.getRole().getType().name(), token};
                });
    }

    /**
     * Handles the sign-up command to register a new user in the system.
     * Validates that the email is unique and that the role type is recognized.
     * Encodes the user password using a hashing service.
     *
     * @param command the sign-up command containing registration details
     * @return the newly created and saved {@link User} entity
     * @throws ResponseStatusException with 409 Conflict if email is already in use,
     *                                 or 400 Bad Request if role validation fails
     */
    @Override
    public User handle(SignUpCommand command) {
        log.info("Sign-up attempt for email: {}", command.email());

        if (userRepository.existsByEmail(command.email())) {
            log.warn("Sign-up rejected — email already registered: {}", command.email());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered: " + command.email());
        }

        RoleType roleType;
        try {
            roleType = RoleType.valueOf(command.role().toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("Sign-up rejected — unknown role: {}", command.role());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown role: " + command.role());
        }

        String passwordHash = hashingService.encode(command.password());
        User user = new User(command.email(), passwordHash, new Role(roleType), null);
        User saved = userRepository.save(user);
        log.info("User registered successfully with ID: {}", saved.getId());
        return saved;
    }
}
