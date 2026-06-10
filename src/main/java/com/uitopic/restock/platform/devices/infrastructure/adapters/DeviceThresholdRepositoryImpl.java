package com.uitopic.restock.platform.devices.infrastructure.adapters;

import com.uitopic.restock.platform.devices.domain.model.entities.DeviceThreshold;
import com.uitopic.restock.platform.devices.domain.repositories.DeviceThresholdRepository;
import com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.assemblers.DeviceThresholdPersistenceAssembler;
import com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.repositories.DeviceThresholdPersistenceRepository;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.DeviceId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the DeviceThresholdRepository interface using MongoDB as the persistence layer.
 */
@Repository
@RequiredArgsConstructor
public class DeviceThresholdRepositoryImpl implements DeviceThresholdRepository {

    // MongoDB repository for DeviceThreshold persistence
    private final DeviceThresholdPersistenceRepository thresholdMongoRepository;

    /**
     * @inheritDocs
     */
    @Override
    public DeviceThreshold save(DeviceThreshold threshold) {
        var saved = thresholdMongoRepository.save(
                DeviceThresholdPersistenceAssembler.toPersistenceFromDomain(threshold)
        );
        return DeviceThresholdPersistenceAssembler.toDomainFromPersistence(saved);
    }

    /**
     * @inheritDocs
     */
    @Override
    public Optional<DeviceThreshold> findById(String id) {
        return thresholdMongoRepository.findById(id)
                .map(DeviceThresholdPersistenceAssembler::toDomainFromPersistence);
    }

    /**
     * @inheritDocs
     */
    @Override
    public Optional<DeviceThreshold> findByDeviceId(DeviceId deviceId) {
        return thresholdMongoRepository.findByDeviceId(deviceId)
                .map(DeviceThresholdPersistenceAssembler::toDomainFromPersistence);
    }

    /**
     * @inheritDocs
     */
    @Override
    public List<DeviceThreshold> findAllByAccountId(AccountId accountId) {
        return thresholdMongoRepository.findAllByAccountId(accountId)
                .stream()
                .map(DeviceThresholdPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    /**
     * @inheritDocs
     */
    @Override
    public void deleteById(String id) {
        thresholdMongoRepository.deleteById(id);
    }

    /**
     * @inheritDocs
     */
    @Override
    public Boolean existsByDeviceId(DeviceId deviceId) {
        return thresholdMongoRepository.existsByDeviceId(deviceId);
    }
}
