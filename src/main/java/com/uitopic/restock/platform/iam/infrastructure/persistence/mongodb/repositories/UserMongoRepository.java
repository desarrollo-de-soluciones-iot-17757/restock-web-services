package com.uitopic.restock.platform.iam.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.iam.domain.model.aggregates.User;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * MongoDB repository for {@link User} aggregates.
 *
 * This interface extends {@link MongoRepository} to provide persistence
 * operations
 * for user entities.
 * It includes custom query methods for finding users by email address and
 * account ID,
 * as well as checking for email existence.
 */
@Repository
public interface UserMongoRepository extends MongoRepository<User, String> {

    /**
     * Finds a user by their email address.
     * <p>
     * This method uses a custom MongoDB query to search for a user by email.
     *
     * @param email the email address to search for
     * @return an {@link Optional} containing the {@link User} if found, otherwise
     *         empty
     */
    @Query("{ 'email.email': ?0 }")
    Optional<User> findByEmailValue(String email);

    /**
     * Checks if a user with the given email address exists.
     * <p>
     * This method uses a custom MongoDB query to efficiently check for the
     * existence
     * of a user with the specified email.
     *
     * @param email the email address to check for existence
     * @return {@code true} if a user with the specified email exists, {@code false}
     *         otherwise
     */
    @Query(value = "{ 'email.email': ?0 }", exists = true)
    boolean existsByEmailValue(String email);

    /**
     * Finds a user by their account ID.
     * <p>
     * This method uses the standard {@link MongoRepository} functionality to search
     * for a user by their account ID.
     *
     * @param accountId the account ID to search for
     * @return an {@link Optional} containing the {@link User} if found, otherwise
     *         empty
     */
    Optional<User> findByAccountId(AccountId accountId);
}
