package com.uitopic.restock.platform.devices.domain.model.commands;

public record ConfirmDeviceConfigurationCommand(String deviceId) {

    public ConfirmDeviceConfigurationCommand {
        if (deviceId == null || deviceId.isBlank())
            throw new IllegalArgumentException("Device ID cannot be null or blank");
    }
}
