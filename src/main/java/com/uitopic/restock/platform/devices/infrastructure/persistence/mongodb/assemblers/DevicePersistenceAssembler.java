package com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.devices.domain.model.aggregates.Device;
import com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.entities.DevicePersistenceEntity;

public final class DevicePersistenceAssembler {

    private DevicePersistenceAssembler() {
        throw new IllegalStateException("Utility class");
    }

    public static Device toDomainFromPersistence(DevicePersistenceEntity entity) {
        if (entity == null) return null;

        var device = new Device();
        device.setId(entity.getId());
        device.setAccountId(entity.getAccountId());
        device.setDeviceToken(entity.getDeviceToken());
        device.setBranchId(entity.getBranchId());
        device.setAssignedBatchId(entity.getAssignedBatchId());
        device.setSupplyThresholdId(entity.getSupplyThresholdId());
        device.setMacAddress(entity.getMacAddress());
        device.setDescription(entity.getDescription());
        device.setSpecifications(entity.getSpecifications());
        device.setWeightMeasurement(entity.getWeightMeasurement());
        device.setJustifiedWithdrawnStock(entity.getJustifiedWithdrawnStock());
        device.setStatus(entity.getStatus());
        device.setDisplayMode(entity.getDisplayMode());

        return device;
    }

    public static DevicePersistenceEntity toPersistenceFromDomain(Device device) {
        if (device == null) return null;

        var entity = new DevicePersistenceEntity();

        if (device.getId() != null) {
            entity.setId(device.getId());
        }
        entity.setAccountId(device.getAccountId());
        entity.setDeviceToken(device.getDeviceToken());
        entity.setBranchId(device.getBranchId());
        entity.setAssignedBatchId(device.getAssignedBatchId());
        entity.setSupplyThresholdId(device.getSupplyThresholdId());
        entity.setMacAddress(device.getMacAddress());
        entity.setDescription(device.getDescription());
        entity.setSpecifications(device.getSpecifications());
        entity.setWeightMeasurement(device.getWeightMeasurement());
        entity.setJustifiedWithdrawnStock(device.getJustifiedWithdrawnStock());
        entity.setStatus(device.getStatus());
        entity.setDisplayMode(device.getDisplayMode());

        return entity;
    }
}
