package com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.entities.DevicePersistenceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevicePersistenceRepository extends MongoRepository<DevicePersistenceEntity, String> {
}
