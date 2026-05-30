package com.uitopic.restock.platform.iam.domain.repositories;

import com.uitopic.restock.platform.iam.domain.model.aggregates.User;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;

import java.util.Optional;

/**
 * Interface defining data access operations for {@link User} aggregates.
 * This repository provides standard CRUD and query methods for user entities.
 */
public interface UserRepository {
    /**
     * Retrieves a user by their email address.
     * 
     * @param email the email address to search for
     * @return an {@link Optional} containing the {@link User} if found, otherwise
     *         empty
     */
    Optional<User> findByEmailValue(String email);

    /**
     * Checks if a user with the given email address already exists.
     * 
     * @param email the email address to check
     * @return {@code true} if a user with the specified email exists, {@code false}
     *         otherwise
     */
    boolean existsByEmailValue(String email);

    /**
     * Saves a user aggregate to the data store.
     * 
     * @param user the user aggregate to save
     * @return the saved {@link User} aggregate
     */
    User save(User user);

    /**
     * Retrieves a user by their unique identifier.
     * 
     * @param id the unique identifier of the user
     * @return an {@link Optional} containing the {@link User} if found, otherwise
     *         empty
     */
    Optional<User> findById(String id);

    /**
     * Retrieves a user by their account ID.
     * 
     * @param accountId the account ID to search for
     * @return an {@link Optional} containing the {@link User} if found, otherwise
     *         empty
     */
    Optional<User> findByAccountId(AccountId accountId);
}
