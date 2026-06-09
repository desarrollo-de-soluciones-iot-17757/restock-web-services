package com.uitopic.restock.platform.devices.infrastructure.adapters;

import com.uitopic.restock.platform.devices.domain.model.aggregates.Device;
import com.uitopic.restock.platform.devices.domain.model.valueobjects.MacAddress;
import com.uitopic.restock.platform.devices.domain.repositories.DeviceRepository;
import com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.assemblers.DevicePersistenceAssembler;
import com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.repositories.DevicePersistenceRepository;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DeviceRepositoryImpl implements DeviceRepository {

    private final DevicePersistenceRepository deviceMongoRepository;

    public DeviceRepositoryImpl(DevicePersistenceRepository devicePersistenceRepository) {
        this.deviceMongoRepository = devicePersistenceRepository;
    }

    @Override
    public Device save(Device device) {
        var saved = deviceMongoRepository.save(DevicePersistenceAssembler.toPersistenceFromDomain(device));
        return DevicePersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public Optional<Device> findById(String id) {
        return deviceMongoRepository.findById(id)
                .map(DevicePersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<Device> findByMacAddress(MacAddress macAddress) {
        return deviceMongoRepository.findByMacAddress(macAddress)
                .map(DevicePersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<Device> findAllByAccountId(AccountId accountId) {
        return deviceMongoRepository.findAllByAccountId(accountId)
                .stream()
                .map(DevicePersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public void deleteById(String id) {
        deviceMongoRepository.deleteById(id);
    }

    @Override
    public boolean existsByMacAddress(MacAddress macAddress) {
        return deviceMongoRepository.existsByMacAddress(macAddress);
    }
}
