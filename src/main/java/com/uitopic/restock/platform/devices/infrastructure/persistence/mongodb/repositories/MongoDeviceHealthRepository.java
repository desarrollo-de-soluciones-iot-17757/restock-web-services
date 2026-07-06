package com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.entities.DeviceHealthPersistenceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data MongoDB repository for DeviceHealthPersistenceEntity.
 */
@Repository
public interface MongoDeviceHealthRepository extends MongoRepository<DeviceHealthPersistenceEntity, String> {

    List<DeviceHealthPersistenceEntity> findByDeviceId(String deviceId);
}
