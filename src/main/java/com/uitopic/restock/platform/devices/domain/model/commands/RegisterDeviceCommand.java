package com.uitopic.restock.platform.devices.domain.model.commands;

public record RegisterDeviceCommand(
        String macAddress,
        String accountId,
        String description
) {
    public RegisterDeviceCommand {
        if (macAddress == null || macAddress.isBlank())
            throw new IllegalArgumentException("MAC address cannot be null or blank");
        if (accountId == null || accountId.isBlank())
            throw new IllegalArgumentException("Account ID cannot be null or blank");
    }
}
