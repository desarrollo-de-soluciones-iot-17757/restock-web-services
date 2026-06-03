package com.uitopic.restock.platform.iam.infrastructure.adapters;

import com.uitopic.restock.platform.iam.domain.model.aggregates.User;
import com.uitopic.restock.platform.iam.domain.model.valueobjects.Email;
import com.uitopic.restock.platform.iam.domain.repositories.UserRepository;
import com.uitopic.restock.platform.iam.infrastructure.persistence.mongodb.assemblers.UserPersistenceAssembler;
import com.uitopic.restock.platform.iam.infrastructure.persistence.mongodb.repositories.UserPersistenceRepository;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link UserRepository} that uses MongoDB for data storage.
 *
 * <p>Acts as a bridge between the domain layer and the MongoDB persistence layer,
 * adapting {@link UserPersistenceRepository} to the {@link UserRepository} port.
 */
@Repository
public class UserRepositoryImpl implements UserRepository {

    // MongoDB repository for user persistence operations
    private final UserPersistenceRepository mongoRepository;

    // Constructor injection of the MongoDB repository
    public UserRepositoryImpl(UserPersistenceRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    /**
     * Finds a user by their email address. Converts the domain {@link Email} value object to a raw string
     *
     * @param email the Email value object to search for
     * @return an {@link Optional} containing the {@link User} if found, otherwise empty
     */
    @Override
    public Optional<User> findByEmail(Email email) {
        return mongoRepository
                .findByEmail(email.email())
                .map(UserPersistenceAssembler::toDomainFromPersistence);
    }

    /**
     * Checks if a user with the given email address exists in the database.
     *
     * @param email the Email value object to check
     * @return {@code true} if a user with the specified email exists, {@code false} otherwise
     */
    @Override
    public boolean existsByEmail(Email email) {
        return mongoRepository.existsByEmail(email.email());
    }

    /**
     * Saves a user aggregate to the database. Converts the domain {@link User} to a persistence entity,
     *
     * @param user the user aggregate to save
     * @return the saved user aggregate with any generated fields (e.g., ID) populated
     */
    @Override
    public User save(User user) {
        var saved = mongoRepository.save(UserPersistenceAssembler.toPersistenceFromDomain(user));
        return UserPersistenceAssembler.toDomainFromPersistence(saved);
    }

    /**
     * Finds a user by their unique identifier.
     *
     * @param id the unique identifier of the user
     * @return an {@link Optional} containing the {@link User} if found, otherwise empty
     */
    @Override
    public Optional<User> findById(String id) {
        return mongoRepository
                .findById(id)
                .map(UserPersistenceAssembler::toDomainFromPersistence);
    }

    /**
     * Finds a user by their account ID.
     *
     * @param accountId the account ID to search for
     * @return an {@link Optional} containing the {@link User} if found, otherwise empty
     */
    @Override
    public Optional<User> findByAccountId(AccountId accountId) {
        return mongoRepository
                .findByAccountId(accountId)
                .map(UserPersistenceAssembler::toDomainFromPersistence);
    }

    /**
     * Finds all users associated with the given account ID.
     *
     * @param accountId the account ID to search for
     * @return a list of {@link User} aggregates for that account
     */
    @Override
    public List<User> findAllByAccountId(AccountId accountId) {
        return mongoRepository
                .findAllByAccountId(accountId)
                .stream()
                .map(UserPersistenceAssembler::toDomainFromPersistence).toList();
    }

    /**
     * Deletes all users from the data store.
     */
    @Override
    public void deleteAll() {
        mongoRepository.deleteAll();
    }
}
