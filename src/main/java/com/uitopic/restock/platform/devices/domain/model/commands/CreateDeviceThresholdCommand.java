package com.uitopic.restock.platform.devices.domain.model.commands;

/**
 * Command to create a new device threshold for a specific custom supply. This command includes validation to ensure that all required fields are provided and that the values are within acceptable ranges.
 */
public record CreateDeviceThresholdCommand(
        String accountId,
        String deviceId,
        String customSupplyId,
        Double minStock,
        Double maxStock,
        Double anomalyThreshold,
        Double minTemperatureCelsius,
        Double maxTemperatureCelsius,
        Double minHumidityPercentage,
        Double maxHumidityPercentage
) {
    public CreateDeviceThresholdCommand {
        if (accountId == null || accountId.isBlank())
            throw new IllegalArgumentException("Account ID cannot be null or blank");
        if (deviceId == null || deviceId.isBlank())
            throw new IllegalArgumentException("Device ID cannot be null or blank");
        if (customSupplyId == null || customSupplyId.isBlank())
            throw new IllegalArgumentException("Custom supply ID cannot be null or blank");
        if (minStock == null || minStock < 0)
            throw new IllegalArgumentException("Min stock cannot be null or negative");
        if (maxStock == null || maxStock <= 0)
            throw new IllegalArgumentException("Max stock must be greater than zero");
        if (minStock >= maxStock)
            throw new IllegalArgumentException("Min stock must be less than max stock");
        if (anomalyThreshold == null || anomalyThreshold < 0)
            throw new IllegalArgumentException("Anomaly threshold cannot be null or negative");
    }
}
