package com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.entities.DiscrepancyPersistenceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing inventory discrepancy persistence entities in MongoDB.
 * This repository provides CRUD operations for DiscrepancyPersistenceEntity objects.
 */
@Repository
public interface DiscrepancyPersistenceRepository extends MongoRepository<DiscrepancyPersistenceEntity, String> {
}
