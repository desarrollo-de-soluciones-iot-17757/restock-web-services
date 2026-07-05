package com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.entities.PhysicalAnomalyPersistenceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data MongoDB repository for PhysicalAnomaly documents.
 */
@Repository
public interface MongoPhysicalAnomalyRepository extends MongoRepository<PhysicalAnomalyPersistenceEntity, String> {
    List<PhysicalAnomalyPersistenceEntity> findByDeviceId(String deviceId);
}
