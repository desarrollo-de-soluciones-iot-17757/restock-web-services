package com.uitopic.restock.platform.iam.infrastructure.repositories;

import com.uitopic.restock.platform.iam.domain.model.aggregates.User;
import com.uitopic.restock.platform.iam.domain.model.valueobjects.Email;
import com.uitopic.restock.platform.iam.domain.repositories.UserRepository;
import com.uitopic.restock.platform.iam.infrastructure.persistence.mongodb.repositories.UserMongoRepository;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link UserRepository} that uses MongoDB for data storage.
 *
 * <p>Acts as a bridge between the domain layer and the MongoDB persistence layer,
 * adapting {@link UserMongoRepository} to the {@link UserRepository} port.
 */
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserMongoRepository mongoRepository;

    public UserRepositoryImpl(UserMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return mongoRepository.findByEmail(email.email());
    }

    @Override
    public boolean existsByEmail(Email email) {
        return mongoRepository.existsByEmail(email.email());
    }

    @Override
    public User save(User user) {
        return mongoRepository.save(user);
    }

    @Override
    public Optional<User> findById(String id) {
        return mongoRepository.findById(id);
    }

    @Override
    public Optional<User> findByAccountId(AccountId accountId) {
        return mongoRepository.findByAccountId(accountId);
    }

    @Override
    public List<User> findAllByAccountId(AccountId accountId) {
        return mongoRepository.findAllByAccountId(accountId);
    }
}
