package com.uitopic.restock.platform.iam.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.iam.domain.model.aggregates.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMongoRepository extends MongoRepository<User, String> {
}
