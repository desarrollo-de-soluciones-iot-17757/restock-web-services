package com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.devices.domain.model.entities.DeviceThreshold;
import com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.entities.DeviceThresholdPersistenceEntity;

public final class DeviceThresholdPersistenceAssembler {

    private DeviceThresholdPersistenceAssembler() {
        // Private constructor to prevent instantiation
    }

    public static DeviceThreshold toDomainFromPersistence(DeviceThresholdPersistenceEntity entity) {
        return null;
    }

    public static DeviceThresholdPersistenceEntity toPersistenceFromDomain(DeviceThreshold deviceThreshold) {
        return null;
    }
}
