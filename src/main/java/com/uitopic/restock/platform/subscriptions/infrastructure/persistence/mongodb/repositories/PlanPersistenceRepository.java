package com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.entities.PlanPersistenceEntity;
import com.uitopic.restock.platform.subscriptions.domain.model.valueobjects.PlanType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanPersistenceRepository extends MongoRepository<PlanPersistenceEntity, String> {
    Optional<PlanPersistenceEntity> findByType(PlanType type);
}
