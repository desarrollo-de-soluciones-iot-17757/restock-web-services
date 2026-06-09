package com.uitopic.restock.platform.tracking.domain.model.valueobjects;

/**
 * Value object representing a device ID used for tracking inventory discrepancies.
 * @param deviceId unique identifier of the device, provided by the request
 */
public record DeviceId(
        String deviceId
) {

    /**
     * Creates a device ID value object.
     *
     * @param deviceId unique identifier of the device, provided by the request
     */
    public DeviceId {
        if (deviceId == null || deviceId.isBlank()) {
            throw new IllegalArgumentException("Device ID cannot be null or blank");
        }
    }

    /**
     * Returns the unique identifier of the device.
     *
     * @return the device ID
     */
    public String getDeviceId() {
        return deviceId;
    }
}
