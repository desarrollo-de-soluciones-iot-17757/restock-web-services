package com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.entities.PlanPersistenceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanPersistenceRepository extends MongoRepository<PlanPersistenceEntity, String> {
}
