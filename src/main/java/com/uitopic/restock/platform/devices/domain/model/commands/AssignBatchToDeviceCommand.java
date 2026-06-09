package com.uitopic.restock.platform.devices.domain.model.commands;

public record AssignBatchToDeviceCommand(
        String deviceId,
        String batchId
) {
    public AssignBatchToDeviceCommand {
        if (deviceId == null || deviceId.isBlank())
            throw new IllegalArgumentException("Device ID cannot be null or blank");
        if (batchId == null || batchId.isBlank())
            throw new IllegalArgumentException("Batch ID cannot be null or blank");
    }
}
