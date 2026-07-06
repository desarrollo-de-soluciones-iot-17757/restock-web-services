package com.uitopic.restock.platform.devices.infrastructure.adapters;

import com.uitopic.restock.platform.devices.domain.model.aggregates.DeviceHealth;
import com.uitopic.restock.platform.devices.domain.repositories.DeviceHealthRepository;
import com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.entities.DeviceHealthPersistenceEntity;
import com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.repositories.MongoDeviceHealthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Adapter implementation of DeviceHealthRepository using MongoDeviceHealthRepository.
 */
@Component
@RequiredArgsConstructor
public class DeviceHealthRepositoryImpl implements DeviceHealthRepository {

    private final MongoDeviceHealthRepository mongoDeviceHealthRepository;

    @Override
    public DeviceHealth save(DeviceHealth deviceHealth) {
        var persistenceEntity = new DeviceHealthPersistenceEntity(deviceHealth);
        var savedEntity = mongoDeviceHealthRepository.save(persistenceEntity);
        return savedEntity.toDomainEntity();
    }

    @Override
    public List<DeviceHealth> findByDeviceId(String deviceId) {
        return mongoDeviceHealthRepository.findByDeviceId(deviceId).stream()
                .map(DeviceHealthPersistenceEntity::toDomainEntity)
                .toList();
    }
}
