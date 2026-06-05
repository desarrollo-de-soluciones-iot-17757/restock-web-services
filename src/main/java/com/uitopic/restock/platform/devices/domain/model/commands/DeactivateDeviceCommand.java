package com.uitopic.restock.platform.devices.domain.model.commands;

public record DeactivateDeviceCommand(String deviceId) {

    public DeactivateDeviceCommand {
        if (deviceId == null || deviceId.isBlank())
            throw new IllegalArgumentException("Device ID cannot be null or blank");
    }
}
