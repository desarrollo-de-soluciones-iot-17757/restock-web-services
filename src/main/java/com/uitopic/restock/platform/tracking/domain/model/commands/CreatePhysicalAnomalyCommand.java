package com.uitopic.restock.platform.tracking.domain.model.commands;

import java.time.Instant;

/**
 * Command to record a physical scale anomaly.
 *
 * @param deviceId Identifier of the submitting device.
 * @param registeredValue Raw registered value from the physical scale.
 * @param timestamp Optional timestamp when anomaly occurred.
 */
public record CreatePhysicalAnomalyCommand(
        String deviceId,
        Double registeredValue,
        Instant timestamp
) {
    public CreatePhysicalAnomalyCommand {
        if (deviceId == null || deviceId.isBlank()) {
            throw new IllegalArgumentException("Device ID cannot be null or blank");
        }
        if (registeredValue == null) {
            throw new IllegalArgumentException("Registered value cannot be null");
        }
    }
}
