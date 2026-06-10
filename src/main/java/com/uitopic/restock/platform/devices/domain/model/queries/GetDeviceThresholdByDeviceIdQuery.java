package com.uitopic.restock.platform.devices.domain.model.queries;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.DeviceId;

/**
 * Query object for retrieving the anomaly threshold associated with a specific device ID.
 *
 * @param deviceId The unique identifier of the device for which to retrieve the anomaly threshold. This value is used to determine when a device's behavior is considered anomalous.
 */
public record GetDeviceThresholdByDeviceIdQuery(DeviceId deviceId) {
}
