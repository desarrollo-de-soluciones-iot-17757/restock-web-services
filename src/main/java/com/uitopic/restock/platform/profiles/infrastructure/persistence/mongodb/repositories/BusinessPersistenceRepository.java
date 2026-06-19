package com.uitopic.restock.platform.profiles.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.profiles.infrastructure.persistence.mongodb.entities.BusinessPersistenceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessPersistenceRepository extends MongoRepository<BusinessPersistenceEntity, String> {
    List<BusinessPersistenceEntity> findByUserId(String userId);
}
