package com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.devices.domain.model.aggregates.Device;
import com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.entities.DevicePersistenceEntity;

public final class DevicePersistenceAssembler {

    private DevicePersistenceAssembler() {
        // Private constructor to prevent instantiation
    }

    public static Device toDomainFromPersistence(DevicePersistenceEntity entity) {
        return null;
    }

    public static DevicePersistenceEntity toPersistenceFromDomain(Device device) {
        return null;
    }
}
