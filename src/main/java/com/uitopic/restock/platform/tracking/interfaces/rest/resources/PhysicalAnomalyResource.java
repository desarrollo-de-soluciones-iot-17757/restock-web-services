package com.uitopic.restock.platform.tracking.interfaces.rest.resources;

import java.time.Instant;

/**
 * Resource DTO representing a saved physical anomaly response.
 */
public record PhysicalAnomalyResource(
        String id,
        String deviceId,
        Double registeredValue,
        Instant timestamp
) {
}
