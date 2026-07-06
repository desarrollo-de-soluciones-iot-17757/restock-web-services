package com.uitopic.restock.platform.tracking.domain.model.queries;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.DeviceId;

/**
 * Query used to retrieve telemetry readings by device ID.
 *
 * @param deviceId device value object identifier
 */
public record GetTelemetryReadingsByDeviceIdQuery(DeviceId deviceId) {
    public GetTelemetryReadingsByDeviceIdQuery {
        if (deviceId == null) {
            throw new IllegalArgumentException("Device ID cannot be null");
        }
    }
}
