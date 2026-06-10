package com.uitopic.restock.platform.devices.domain.services;

import com.uitopic.restock.platform.devices.domain.model.entities.DeviceThreshold;
import com.uitopic.restock.platform.devices.domain.model.queries.GetDeviceThresholdByDeviceIdQuery;
import com.uitopic.restock.platform.devices.domain.model.queries.GetDeviceThresholdByIdQuery;
import com.uitopic.restock.platform.devices.domain.model.queries.GetDeviceThresholdsByAccountIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for handling queries related to DeviceThreshold entities.
 */
public interface DeviceThresholdQueryService {

    /**
     * Handles the query to retrieve a DeviceThreshold by its unique identifier.
     *
     * @param query the query containing the ID of the DeviceThreshold to retrieve
     * @return an Optional containing the DeviceThreshold if found, or empty if not found
     */
    Optional<DeviceThreshold> handle(GetDeviceThresholdByIdQuery query);

    /**
     * Handles the query to retrieve a DeviceThreshold by the associated device ID.
     *
     * @param query the query containing the device ID to search for
     * @return an Optional containing the DeviceThreshold if found, or empty if not found
     */
    Optional<DeviceThreshold> handle(GetDeviceThresholdByDeviceIdQuery query);

    /**
     * Handles the query to retrieve all DeviceThresholds associated with a specific account ID.
     *
     * @param query the query containing the account ID to search for
     * @return a list of DeviceThresholds associated with the given account ID, or an empty list if none are found
     */
    List<DeviceThreshold> handle(GetDeviceThresholdsByAccountIdQuery query);
}
