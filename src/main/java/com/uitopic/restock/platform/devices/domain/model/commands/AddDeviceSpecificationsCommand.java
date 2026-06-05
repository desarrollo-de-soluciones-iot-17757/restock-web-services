package com.uitopic.restock.platform.devices.domain.model.commands;

public record AddDeviceSpecificationsCommand(
        String deviceId,
        String manufacturer,
        String model,
        String firmwareVersion
) {
    public AddDeviceSpecificationsCommand {
        if (deviceId == null || deviceId.isBlank())
            throw new IllegalArgumentException("Device ID cannot be null or blank");
        if (manufacturer == null || manufacturer.isBlank())
            throw new IllegalArgumentException("Manufacturer cannot be null or blank");
        if (model == null || model.isBlank())
            throw new IllegalArgumentException("Model cannot be null or blank");
        if (firmwareVersion == null || firmwareVersion.isBlank())
            throw new IllegalArgumentException("Firmware version cannot be null or blank");
    }
}
