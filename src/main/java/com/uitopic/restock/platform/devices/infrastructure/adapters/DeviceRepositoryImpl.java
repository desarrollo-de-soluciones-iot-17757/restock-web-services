package com.uitopic.restock.platform.devices.infrastructure.adapters;

import com.uitopic.restock.platform.devices.domain.model.aggregates.Device;
import com.uitopic.restock.platform.devices.domain.model.valueobjects.MacAddress;
import com.uitopic.restock.platform.devices.domain.repositories.DeviceRepository;
import com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.assemblers.DevicePersistenceAssembler;
import com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.repositories.DevicePersistenceRepository;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * DeviceRepositoryImpl is an implementation of the DeviceRepository interface. It serves as a repository for managing device entities, providing methods to save, find, and delete devices in the database. It uses the DevicePersistenceRepository to perform database operations and the DevicePersistenceAssembler to convert between domain models and persistence models.
 */
@Repository
@RequiredArgsConstructor
public class DeviceRepositoryImpl implements DeviceRepository {

    // The DevicePersistenceRepository is injected to handle database operations related to devices.
    private final DevicePersistenceRepository deviceMongoRepository;

    /**
     * @inheritDocs
     */
    @Override
    public Device save(Device device) {
        var saved = deviceMongoRepository.save(DevicePersistenceAssembler.toPersistenceFromDomain(device));
        return DevicePersistenceAssembler.toDomainFromPersistence(saved);
    }

    /**
     * @inheritDocs
     */
    @Override
    public Optional<Device> findById(String id) {
        return deviceMongoRepository.findById(id)
                .map(DevicePersistenceAssembler::toDomainFromPersistence);
    }

    /**
     * @inheritDocs
     */
    @Override
    public Optional<Device> findByMacAddress(MacAddress macAddress) {
        return deviceMongoRepository.findByMacAddress(macAddress)
                .map(DevicePersistenceAssembler::toDomainFromPersistence);
    }

    /**
     * @inheritDocs
     */
    @Override
    public List<Device> findAllByAccountId(AccountId accountId) {
        return deviceMongoRepository.findAllByAccountId(accountId)
                .stream()
                .map(DevicePersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    /**
     * @inheritDocs
     */
    @Override
    public void deleteById(String id) {
        deviceMongoRepository.deleteById(id);
    }

    /**
     * @inheritDocs
     */
    @Override
    public Boolean existsByMacAddress(MacAddress macAddress) {
        return deviceMongoRepository.existsByMacAddress(macAddress);
    }

    /**
     * @inheritDocs
     */
    @Override
    public Boolean existsByDeviceId(String deviceId) {
        if (deviceId == null || deviceId.isBlank()) {
            return false;
        }
        if (deviceMongoRepository.existsById(deviceId)) {
            return true;
        }
        try {
            return deviceMongoRepository.existsByMacAddress(new MacAddress(deviceId));
        } catch (Exception e) {
            return false;
        }
    }
}
