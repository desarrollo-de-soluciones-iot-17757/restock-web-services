package com.uitopic.restock.platform.iam.domain.repositories;

import com.uitopic.restock.platform.iam.domain.model.aggregates.User;
import com.uitopic.restock.platform.iam.domain.model.valueobjects.Email;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;

import java.util.List;
import java.util.Optional;

/**
 * Interface defining data access operations for {@link User} aggregates.
 */
public interface UserRepository {

    /**
     * Retrieves a user by their email address.
     *
     * @param email the Email value object to search for
     * @return an {@link Optional} containing the {@link User} if found, otherwise empty
     */
    Optional<User> findByEmail(Email email);

    /**
     * Checks if a user with the given email address already exists.
     *
     * @param email the Email value object to check
     * @return {@code true} if a user with the specified email exists, {@code false} otherwise
     */
    boolean existsByEmail(Email email);

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
     * @return an {@link Optional} containing the {@link User} if found, otherwise empty
     */
    Optional<User> findById(String id);

    /**
     * Retrieves a user by their account ID.
     *
     * @param accountId the account ID to search for
     * @return an {@link Optional} containing the {@link User} if found, otherwise empty
     */
    Optional<User> findByAccountId(AccountId accountId);

    /**
     * Retrieves all users associated with the given account ID.
     * Supports accounts with multiple worker users.
     *
     * @param accountId the account ID to search for
     * @return a {@link List} of {@link User} aggregates for that account
     */
    List<User> findAllByAccountId(AccountId accountId);
}
