package com.uitopic.restock.platform.devices.domain.model.commands;

import com.uitopic.restock.platform.devices.domain.model.valueobjects.DisplayMode;

public record UpdateDeviceDisplayModeCommand(
        String deviceId,
        DisplayMode displayMode
) {
    public UpdateDeviceDisplayModeCommand {
        if (deviceId == null || deviceId.isBlank())
            throw new IllegalArgumentException("Device ID cannot be null or blank");
        if (displayMode == null)
            throw new IllegalArgumentException("Display mode cannot be null");
    }
}
