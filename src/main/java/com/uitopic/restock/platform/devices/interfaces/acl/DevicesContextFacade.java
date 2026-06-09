package com.uitopic.restock.platform.devices.interfaces.acl;

import com.uitopic.restock.platform.tracking.domain.model.valueobjects.DeviceId;

/**
 * Facade interface for accessing device-related information and operations in the Devices Context.
 * This interface abstracts the underlying implementation details and provides a simplified API for clients.
 */
public interface DevicesContextFacade {

    /**
     * Retrieves the anomaly threshold for a given device ID.
     *
     * @param deviceId The unique identifier of the device for which to retrieve the anomaly threshold.
     * @return The anomaly threshold value associated with the specified device ID. This value is used to determine when a device's behavior is considered anomalous.
     */
    Double getAnomalyThresholdByDeviceId(DeviceId deviceId);
}
