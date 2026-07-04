package com.uitopic.restock.platform.tracking.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

/**
 * Resource DTO for receiving physical anomaly registration requests.
 *
 * @param deviceId Unique ID of the device.
 * @param registeredValue Registered raw weight or physical value.
 * @param timestamp Timestamp of the anomaly (optional, defaults to now).
 */
public record CreatePhysicalAnomalyResource(
        @NotBlank(message = "Device ID is required")
        String deviceId,

        @NotNull(message = "Registered value is required")
        Double registeredValue,

        Instant timestamp
) {
}
