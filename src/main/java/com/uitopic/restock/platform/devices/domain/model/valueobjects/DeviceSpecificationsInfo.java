package com.uitopic.restock.platform.devices.domain.model.valueobjects;

public record DeviceSpecificationsInfo(
        String manufacturer,
        String model,
        String firmwareVersion
) {
    public DeviceSpecificationsInfo {
        if (manufacturer == null || manufacturer.isBlank())
            throw new IllegalArgumentException("Manufacturer cannot be null or blank");
        if (model == null || model.isBlank())
            throw new IllegalArgumentException("Model cannot be null or blank");
        if (firmwareVersion == null || firmwareVersion.isBlank())
            throw new IllegalArgumentException("Firmware version cannot be null or blank");
    }
}
