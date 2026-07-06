package com.uitopic.restock.platform.devices.domain.model.queries;

/**
 * Query used to retrieve device health logs by device ID.
 *
 * @param deviceId device unique identifier
 */
public record GetDeviceHealthLogsByDeviceIdQuery(String deviceId) {
    public GetDeviceHealthLogsByDeviceIdQuery {
        if (deviceId == null || deviceId.isBlank()) {
            throw new IllegalArgumentException("Device ID cannot be null or blank");
        }
    }
}
