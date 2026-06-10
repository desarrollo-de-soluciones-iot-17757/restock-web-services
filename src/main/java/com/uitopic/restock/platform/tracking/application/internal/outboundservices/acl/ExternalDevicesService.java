package com.uitopic.restock.platform.tracking.application.internal.outboundservices.acl;

import com.uitopic.restock.platform.devices.interfaces.acl.DevicesContextFacade;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.DeviceId;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for interacting with the Devices Context to perform operations related to external devices in the tracking context.
 */
@Service(value = "trackingExternalDevicesService")
@RequiredArgsConstructor
public class ExternalDevicesService {

    // Inject the Devices Context Facade to access device-related information and operations
    private final DevicesContextFacade devicesContextFacade;

    /**
     * Checks if a device with the given device ID exists in the system by delegating to the Devices Context Facade.
     *
     * @param deviceId The unique identifier of the device to check for existence.
     * @return true if a device with the specified device ID exists, false otherwise.
     */
    public Boolean deviceExists(DeviceId deviceId) {
        return devicesContextFacade.existsByDeviceId(deviceId);
    }

    /**
     * Retrieves the anomaly threshold for a given device ID by delegating to the Devices Context Facade.
     *
     * @param deviceId The unique identifier of the device for which to retrieve the anomaly threshold.
     * @return The anomaly threshold value associated with the specified device ID. This value is used to determine when a device's behavior is considered anomalous.
     */
    public Pair<Double, AccountId> getAnomalyThreshold(DeviceId deviceId) {
        return devicesContextFacade.getAnomalyThresholdByDeviceId(deviceId);
    }
}
