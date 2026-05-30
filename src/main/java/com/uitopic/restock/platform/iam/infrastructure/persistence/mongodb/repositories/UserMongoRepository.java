package com.uitopic.restock.platform.iam.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.iam.domain.model.aggregates.User;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB repository for {@link User} aggregates.
 *
 * <p>Email is stored as a plain string field (not as an embedded document),
 * so Spring Data derives the query directly from the field name {@code email}
 * without any custom {@code @Query} annotation.
 */
@Repository
public interface UserMongoRepository extends MongoRepository<User, String> {

    /**
     * Finds a user by their email address.
     * Relies on the {@code email} field being stored as a primitive string value.
     *
     * @param email the email address to search for
     * @return an {@link Optional} containing the {@link User} if found, otherwise empty
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user with the given email address exists.
     * Relies on the {@code email} field being stored as a primitive string value.
     *
     * @param email the email address to check for existence
     * @return {@code true} if a user with the specified email exists, {@code false} otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Finds a user by their account ID.
     *
     * @param accountId the account ID to search for
     * @return an {@link Optional} containing the {@link User} if found, otherwise empty
     */
    Optional<User> findByAccountId(AccountId accountId);

    /**
     * Finds all users associated with the given account ID.
     * Supports accounts with multiple worker users.
     *
     * @param accountId the account ID to search for
     * @return a {@link List} of {@link User} aggregates for that account
     */
    List<User> findAllByAccountId(AccountId accountId);
}
