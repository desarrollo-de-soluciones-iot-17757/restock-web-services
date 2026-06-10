package com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.entities.TelemetryReadingPersistenceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing telemetry reading persistence entities in MongoDB.
 * This interface extends MongoRepository, providing CRUD operations for TelemetryReadingPersistenceEntity.
 */
@Repository
public interface TelemetryReadingPersistenceRepository extends MongoRepository<TelemetryReadingPersistenceEntity, String> {
}
