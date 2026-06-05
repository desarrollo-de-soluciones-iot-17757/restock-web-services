package com.uitopic.restock.platform.devices.domain.model.commands;

public record UpdateJustifiedWithdrawnStockCommand(
        String deviceId,
        Double amount
) {
    public UpdateJustifiedWithdrawnStockCommand {
        if (deviceId == null || deviceId.isBlank())
            throw new IllegalArgumentException("Device ID cannot be null or blank");
        if (amount == null || amount < 0)
            throw new IllegalArgumentException("Amount cannot be null or negative");
    }
}
