package com.uitopic.restock.platform.devices.interfaces.rest.transform;

import com.uitopic.restock.platform.devices.domain.model.entities.DeviceThreshold;
import com.uitopic.restock.platform.devices.interfaces.rest.resources.DeviceThresholdResource;

public class DeviceThresholdResourceFromEntityAssembler {

    private DeviceThresholdResourceFromEntityAssembler() {
        throw new IllegalStateException("Utility class");
    }

    public static DeviceThresholdResource toResourceFromEntity(DeviceThreshold threshold) {
        var temp = threshold.getTemperature();
        var humidity = threshold.getHumidity();

        return new DeviceThresholdResource(
                threshold.getId(),
                threshold.getAccountId() != null ? threshold.getAccountId().getAccountId() : null,
                threshold.getCustomSupplyId(),
                threshold.getMinStock(),
                threshold.getMaxStock(),
                threshold.getAnomalyThreshold(),
                temp != null ? temp.minCelsius() : null,
                temp != null ? temp.maxCelsius() : null,
                humidity != null ? humidity.minPercentage() : null,
                humidity != null ? humidity.maxPercentage() : null
        );
    }
}
