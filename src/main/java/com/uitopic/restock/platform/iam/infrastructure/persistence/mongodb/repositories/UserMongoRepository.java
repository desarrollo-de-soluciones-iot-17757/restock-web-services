package com.uitopic.restock.platform.iam.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.iam.domain.model.aggregates.User;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB repository for {@link User} aggregates.
 *
 * <p>The {@code email} field is stored as a primitive string value in MongoDB
 * via the registered {@link com.uitopic.restock.platform.iam.infrastructure.persistence.mongodb.converters.EmailWriteConverter}.
 * Queries use {@code String} parameters with explicit {@link Query} annotations to target
 * the stored primitive field directly — Spring Data query derivation does not apply
 * custom converters to query parameters, so the value object must be unwrapped here.
 */
@Repository
public interface UserMongoRepository extends MongoRepository<User, String> {

    /**
     * Finds a user by their email address.
     * Uses an explicit query to match against the primitive {@code email} string field.
     *
     * @param email the raw email string to search for
     * @return an {@link Optional} containing the {@link User} if found, otherwise empty
     */
    @Query("{ 'email' : ?0 }")
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user with the given email address exists.
     * Uses an explicit query to match against the primitive {@code email} string field.
     *
     * @param email the raw email string to check
     * @return {@code true} if a user with the specified email exists, {@code false} otherwise
     */
    @Query(value = "{ 'email' : ?0 }", exists = true)
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
