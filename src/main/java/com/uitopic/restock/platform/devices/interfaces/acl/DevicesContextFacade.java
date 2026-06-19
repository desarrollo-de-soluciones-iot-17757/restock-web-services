package com.uitopic.restock.platform.devices.interfaces.acl;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.DeviceId;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Facade interface for accessing device-related information and operations in the Devices Context.
 * This interface abstracts the underlying implementation details and provides a simplified API for clients.
 */
public interface DevicesContextFacade {

    /**
     * Checks if a device with the given device ID exists in the system.
     *
     * @param deviceId The unique identifier of the device to check for existence.
     * @return true if a device with the specified device ID exists, false otherwise.
     */
    Boolean existsByDeviceId(DeviceId deviceId);

    /**
     * Retrieves the anomaly threshold for a given device ID.
     *
     * @param deviceId The unique identifier of the device for which to retrieve the anomaly threshold.
     * @return The anomaly threshold value associated with the specified device ID. This value is used to determine when a device's behavior is considered anomalous.
     */
    Pair<Double, AccountId> getAnomalyThresholdByDeviceId(DeviceId deviceId);
    Pair<Double, AccountId> getJustifiedWithdrawnStockByDeviceId(DeviceId deviceId);
    void updateJustifiedWithdrawnStock(DeviceId deviceId, Double amount);
    void recalibrateDevice(DeviceId deviceId);
}
