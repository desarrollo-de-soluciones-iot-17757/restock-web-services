package com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.devices.domain.model.entities.DeviceThreshold;
import com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.entities.DeviceThresholdPersistenceEntity;

public final class DeviceThresholdPersistenceAssembler {

    private DeviceThresholdPersistenceAssembler() {
        throw new IllegalStateException("Utility class");
    }

    public static DeviceThreshold toDomainFromPersistence(DeviceThresholdPersistenceEntity entity) {
        if (entity == null) return null;

        var threshold = new DeviceThreshold();
        threshold.setId(entity.getId());
        threshold.setAccountId(entity.getAccountId());
        threshold.setCustomSupplyId(entity.getCustomSupplyId());
        threshold.setMinStock(entity.getMinStock());
        threshold.setMaxStock(entity.getMaxStock());
        threshold.setAnomalyThreshold(entity.getAnomalyThreshold());
        threshold.setTemperature(entity.getTemperature());
        threshold.setHumidity(entity.getHumidity());

        return threshold;
    }

    public static DeviceThresholdPersistenceEntity toPersistenceFromDomain(DeviceThreshold threshold) {
        if (threshold == null) return null;

        var entity = new DeviceThresholdPersistenceEntity();

        if (threshold.getId() != null) {
            entity.setId(threshold.getId());
        }
        entity.setAccountId(threshold.getAccountId());
        entity.setCustomSupplyId(threshold.getCustomSupplyId());
        entity.setMinStock(threshold.getMinStock());
        entity.setMaxStock(threshold.getMaxStock());
        entity.setAnomalyThreshold(threshold.getAnomalyThreshold());
        entity.setTemperature(threshold.getTemperature());
        entity.setHumidity(threshold.getHumidity());

        return entity;
    }
}
