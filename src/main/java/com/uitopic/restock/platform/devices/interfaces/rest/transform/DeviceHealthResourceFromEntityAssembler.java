package com.uitopic.restock.platform.devices.interfaces.rest.transform;

import com.uitopic.restock.platform.devices.domain.model.aggregates.DeviceHealth;
import com.uitopic.restock.platform.devices.interfaces.rest.resources.DeviceHealthResource;

public class DeviceHealthResourceFromEntityAssembler {

    public static DeviceHealthResource toResourceFromEntity(DeviceHealth entity) {
        return new DeviceHealthResource(
                entity.getId(),
                entity.getDeviceId(),
                entity.getBranchId(),
                entity.getAlertType(),
                entity.getMetric(),
                entity.getValue(),
                entity.getThreshold(),
                entity.getMessage(),
                entity.getCpuUsagePercentage(),
                entity.getMemoryFreeBytes(),
                entity.getVoltageVolts(),
                entity.getTemperatureInCelsius(),
                entity.getTimestamp()
        );
    }
}
