package com.uitopic.restock.platform.devices.domain.model.commands;

public record AssignSupplyThresholdCommand(
        String deviceId,
        String supplyThresholdId
) {
    public AssignSupplyThresholdCommand {
        if (deviceId == null || deviceId.isBlank())
            throw new IllegalArgumentException("Device ID cannot be null or blank");
        if (supplyThresholdId == null || supplyThresholdId.isBlank())
            throw new IllegalArgumentException("Supply threshold ID cannot be null or blank");
    }
}
