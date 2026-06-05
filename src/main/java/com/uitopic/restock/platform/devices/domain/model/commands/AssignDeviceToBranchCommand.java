package com.uitopic.restock.platform.devices.domain.model.commands;

public record AssignDeviceToBranchCommand(
        String deviceId,
        String branchId
) {
    public AssignDeviceToBranchCommand {
        if (deviceId == null || deviceId.isBlank())
            throw new IllegalArgumentException("Device ID cannot be null or blank");
        if (branchId == null || branchId.isBlank())
            throw new IllegalArgumentException("Branch ID cannot be null or blank");
    }
}
