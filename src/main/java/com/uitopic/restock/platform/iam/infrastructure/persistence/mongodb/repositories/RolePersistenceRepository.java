package com.uitopic.restock.platform.iam.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.iam.infrastructure.persistence.mongodb.entities.RolePersistenceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePersistenceRepository extends MongoRepository<RolePersistenceEntity, String> {
}
