package com.uitopic.restock.platform.iam.domain.services;

import com.uitopic.restock.platform.iam.domain.model.aggregates.User;
import com.uitopic.restock.platform.iam.domain.model.commands.SignInCommand;
import com.uitopic.restock.platform.iam.domain.model.commands.SignUpCommand;

import java.util.Optional;

/**
 * Service interface for handling user-related commands.
 * Defines operations for user authentication and registration.
 */
public interface UserCommandService {

    /**
     * Handles the {@link SignInCommand} to authenticate a user.
     * 
     * @param command the sign-in command containing credentials
     * @return an {@link Optional} containing the {@link User} if authentication is
     *         successful, otherwise empty
     */
    Optional<User> handle(SignInCommand command);

    /**
     * Handles the {@link SignUpCommand} to register a new user.
     * 
     * @param command the sign-up command containing user registration details
     * @return the newly created {@link User} aggregate
     */
    User handle(SignUpCommand command);
}
