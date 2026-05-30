package com.uitopic.restock.platform.iam.infrastructure.repositories;

import com.uitopic.restock.platform.iam.domain.model.aggregates.User;
import com.uitopic.restock.platform.iam.domain.repositories.UserRepository;
import com.uitopic.restock.platform.iam.infrastructure.persistence.mongodb.repositories.UserMongoRepository;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Implementation of {@link UserRepository} that uses MongoDB for data storage.
 * <p>
 * This class acts as a bridge between the domain layer and the MongoDB
 * persistence layer.
 * It adapts the {@link UserMongoRepository} to the {@link UserRepository}
 * interface, providing persistence operations for {@link User} aggregates.
 */
@Repository
public class UserRepositoryImpl implements UserRepository {

    /**
     * The MongoDB repository for {@link User} aggregates.
     */
    private final UserMongoRepository mongoRepository;

    /**
     * Constructor for {@code UserRepositoryImpl}.
     * 
     * @param mongoRepository the MongoDB repository for {@link User} aggregates
     */
    public UserRepositoryImpl(UserMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    /**
     * Finds a user by their email address.
     * 
     * @param email the email address to search for
     * @return an {@link Optional} containing the {@link User} if found, otherwise
     *         empty
     */
    @Override
    public Optional<User> findByEmailValue(String email) {
        return mongoRepository.findByEmailValue(email);
    }

    /**
     * Checks if a user with the given email address exists.
     * 
     * @param email the email address to check for existence
     * @return {@code true} if a user with the specified email exists, {@code false}
     *         otherwise
     */
    @Override
    public boolean existsByEmailValue(String email) {
        return mongoRepository.existsByEmailValue(email);
    }

    /**
     * Saves a {@link User} aggregate to the database.
     * 
     * @param user the {@link User} aggregate to save
     * @return the saved {@link User} aggregate
     */
    @Override
    public User save(User user) {
        return mongoRepository.save(user);
    }

    /**
     * Finds a user by their unique identifier.
     * 
     * @param id the unique identifier of the user
     * @return an {@link Optional} containing the {@link User} if found, otherwise
     *         empty
     */
    @Override
    public Optional<User> findById(String id) {
        return mongoRepository.findById(id);
    }

    /**
     * Finds a user by their account ID.
     * 
     * @param accountId the account ID to search for
     * @return an {@link Optional} containing the {@link User} if found, otherwise
     *         empty
     */
    @Override
    public Optional<User> findByAccountId(AccountId accountId) {
        return mongoRepository.findByAccountId(accountId);
    }
}
