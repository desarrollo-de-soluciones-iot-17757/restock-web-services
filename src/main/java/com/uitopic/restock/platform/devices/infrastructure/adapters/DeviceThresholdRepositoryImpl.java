package com.uitopic.restock.platform.devices.infrastructure.adapters;

import com.uitopic.restock.platform.devices.domain.model.entities.DeviceThreshold;
import com.uitopic.restock.platform.devices.domain.repositories.DeviceThresholdRepository;
import com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.assemblers.DeviceThresholdPersistenceAssembler;
import com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.repositories.DeviceThresholdPersistenceRepository;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DeviceThresholdRepositoryImpl implements DeviceThresholdRepository {

    private final DeviceThresholdPersistenceRepository thresholdMongoRepository;

    public DeviceThresholdRepositoryImpl(DeviceThresholdPersistenceRepository thresholdPersistenceRepository) {
        this.thresholdMongoRepository = thresholdPersistenceRepository;
    }

    @Override
    public DeviceThreshold save(DeviceThreshold threshold) {
        var saved = thresholdMongoRepository.save(
                DeviceThresholdPersistenceAssembler.toPersistenceFromDomain(threshold)
        );
        return DeviceThresholdPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public Optional<DeviceThreshold> findById(String id) {
        return thresholdMongoRepository.findById(id)
                .map(DeviceThresholdPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<DeviceThreshold> findAllByAccountId(AccountId accountId) {
        return thresholdMongoRepository.findAllByAccountId(accountId)
                .stream()
                .map(DeviceThresholdPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public void deleteById(String id) {
        thresholdMongoRepository.deleteById(id);
    }
}
