package com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.entities.DeviceThresholdPersistenceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceThresholdPersistenceRepository extends MongoRepository<DeviceThresholdPersistenceEntity, String> {
}
