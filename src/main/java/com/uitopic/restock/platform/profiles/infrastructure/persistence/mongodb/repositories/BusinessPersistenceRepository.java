package com.uitopic.restock.platform.profiles.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.profiles.infrastructure.persistence.mongodb.entities.BusinessPersistenceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessPersistenceRepository extends MongoRepository<BusinessPersistenceEntity, String> {
}
