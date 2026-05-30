package com.uitopic.restock.platform.iam.infrastructure.repositories;

import com.uitopic.restock.platform.iam.domain.model.aggregates.User;
import com.uitopic.restock.platform.iam.domain.repositories.UserRepository;
import com.uitopic.restock.platform.iam.infrastructure.persistence.mongodb.repositories.UserMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserMongoRepository mongoRepository;

    public UserRepositoryImpl(UserMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Optional<User> findByEmailValue(String email) {
        return mongoRepository.findByEmailValue(email);
    }

    @Override
    public boolean existsByEmailValue(String email) {
        return mongoRepository.existsByEmailValue(email);
    }

    @Override
    public User save(User user) {
        return mongoRepository.save(user);
    }

    @Override
    public Optional<User> findById(String id) {
        return mongoRepository.findById(id);
    }
}
