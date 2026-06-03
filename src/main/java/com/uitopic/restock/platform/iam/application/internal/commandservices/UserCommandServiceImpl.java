package com.uitopic.restock.platform.iam.application.internal.commandservices;

import com.uitopic.restock.platform.iam.application.internal.outboundservices.acl.ExternalProfilesService;
import com.uitopic.restock.platform.iam.application.internal.outboundservices.hashing.HashingService;
import com.uitopic.restock.platform.iam.application.internal.outboundservices.tokens.TokenService;
import com.uitopic.restock.platform.iam.domain.model.aggregates.User;
import com.uitopic.restock.platform.iam.domain.model.commands.SignInCommand;
import com.uitopic.restock.platform.iam.domain.model.commands.SignUpCommand;
import com.uitopic.restock.platform.iam.domain.model.entities.Role;
import com.uitopic.restock.platform.iam.domain.model.valueobjects.Email;
import com.uitopic.restock.platform.iam.domain.model.valueobjects.RoleType;
import com.uitopic.restock.platform.iam.domain.repositories.UserRepository;
import com.uitopic.restock.platform.iam.domain.services.UserCommandService;
import com.uitopic.restock.platform.profiles.interfaces.acl.ProfilesContextFacade;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.bson.types.ObjectId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.AbstractMap;
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

    // The user repository is used to persist and retrieve user entities from the database
    private final UserRepository userRepository;

    // The hashing service is used to encode passwords and verify password matches
    private final HashingService hashingService;

    // The token service is responsible for generating JWT tokens upon successful authentication
    private final TokenService tokenService;

    // The external profiles service is used to orchestrate profile creation in the Profiles bounded context via the ACL
    private final ExternalProfilesService externalProfilesService;

    // Constructor injection of dependencies
    public UserCommandServiceImpl(UserRepository userRepository,
                                  HashingService hashingService,
                                  TokenService tokenService,
                                  ExternalProfilesService externalProfilesService) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
        this.externalProfilesService = externalProfilesService;
    }

    /**
     * Handles the sign-in command to authenticate a user.
     * Token generation is performed here in the application layer.
     *
     * @param command the sign-in command containing the credentials
     * @return an {@link Optional} containing a pair of the authenticated {@link User}
     *         and the generated JWT token, or empty if credentials are invalid
     */
    @Override
    public Optional<AbstractMap.SimpleEntry<User, String>> handle(SignInCommand command) {
        log.info("Sign-in attempt for email: {}", command.email());
        return userRepository.findByEmail(new Email(command.email()))
                .filter(user -> {
                    boolean matches = hashingService.matches(command.password(), user.getPasswordHash());
                    if (!matches) log.warn("Invalid password for email: {}", command.email());
                    return matches;
                })
                .map(user -> {
                    String token = tokenService.generateToken(user);
                    log.info("Sign-in successful for user ID: {}", user.getId());
                    return new AbstractMap.SimpleEntry<>(user, token);
                });
    }

    /**
     * Handles the sign-up command to register a new user in the system.
     * Validates that the email is unique and that the role type is recognized.
     * Encodes the user password using a hashing service.
     * Orchestrates profile creation using the Profiles Bounded Context ACL.
     *
     * @param command the sign-up command containing registration details
     * @return the newly created and saved {@link User} entity
     * @throws ResponseStatusException with 409 Conflict if email is already in use,
     *                                 or 400 Bad Request if email format or role is invalid
     */
    @Override
    public User handle(SignUpCommand command) {
        log.info("Sign-up attempt for email: {}", command.email());

        Email email;
        try {
            email = new Email(command.email());
        } catch (IllegalArgumentException e) {
            log.warn("Sign-up rejected — invalid email format: {}", command.email());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        if (userRepository.existsByEmail(email)) {
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
        String generatedAccountId = new ObjectId().toHexString();
        User user = new User(email, passwordHash, new Role(roleType), new AccountId(generatedAccountId));
        User saved = userRepository.save(user);
        log.info("User registered successfully with ID: {} and accountId: {}", saved.getId(), generatedAccountId);

        log.info("Creating profile for user ID: {} via ProfilesContextFacade ACL", saved.getId());
        try {
            String profileId = externalProfilesService.createProfileForNewUser(
                    saved.getId(),
                    command.businessName(),
                    command.email(),
                    command.phone(),
                    command.country()
            );
            log.info("Successfully created profile with ID: '{}' for user ID: {}", profileId, saved.getId());
        } catch (Exception e) {
            log.error("Failed to create profile for user ID: {}", saved.getId(), e);
        }

        return saved;
    }
}
