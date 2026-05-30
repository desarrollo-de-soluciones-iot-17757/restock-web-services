package com.uitopic.restock.platform.iam.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.iam.domain.model.aggregates.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserMongoRepository extends MongoRepository<User, String> {

    @Query("{ 'email.email': ?0 }")
    Optional<User> findByEmailValue(String email);

    @Query(value = "{ 'email.email': ?0 }", exists = true)
    boolean existsByEmailValue(String email);
}
