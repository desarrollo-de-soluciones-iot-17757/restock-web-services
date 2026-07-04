package com.uitopic.restock.platform.tracking.infrastructure.adapters;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.DeviceId;
import com.uitopic.restock.platform.tracking.domain.model.aggregates.PhysicalAnomaly;
import com.uitopic.restock.platform.tracking.domain.repositories.PhysicalAnomalyRepository;
import com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.entities.PhysicalAnomalyPersistenceEntity;
import com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.repositories.MongoPhysicalAnomalyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adapter implementing PhysicalAnomalyRepository using MongoPhysicalAnomalyRepository.
 */
@Component
@RequiredArgsConstructor
public class PhysicalAnomalyRepositoryImpl implements PhysicalAnomalyRepository {

    private final MongoPhysicalAnomalyRepository mongoRepository;

    @Override
    public PhysicalAnomaly save(PhysicalAnomaly anomaly) {
        PhysicalAnomalyPersistenceEntity entity = new PhysicalAnomalyPersistenceEntity(
                anomaly.getId(),
                anomaly.getDeviceId() != null ? anomaly.getDeviceId().getDeviceId() : null,
                anomaly.getRegisteredValue(),
                anomaly.getTimestamp()
        );

        PhysicalAnomalyPersistenceEntity saved = mongoRepository.save(entity);

        PhysicalAnomaly domain = new PhysicalAnomaly(
                new DeviceId(saved.getDeviceId()),
                saved.getRegisteredValue(),
                saved.getTimestamp()
        );
        domain.setId(saved.getId());
        return domain;
    }

    @Override
    public Optional<PhysicalAnomaly> findById(String id) {
        return mongoRepository.findById(id).map(entity -> {
            PhysicalAnomaly domain = new PhysicalAnomaly(
                    new DeviceId(entity.getDeviceId()),
                    entity.getRegisteredValue(),
                    entity.getTimestamp()
            );
            domain.setId(entity.getId());
            return domain;
        });
    }

    @Override
    public List<PhysicalAnomaly> findByDeviceId(String deviceId) {
        return mongoRepository.findByDeviceId(deviceId).stream().map(entity -> {
            PhysicalAnomaly domain = new PhysicalAnomaly(
                    new DeviceId(entity.getDeviceId()),
                    entity.getRegisteredValue(),
                    entity.getTimestamp()
            );
            domain.setId(entity.getId());
            return domain;
        }).toList();
    }
}
