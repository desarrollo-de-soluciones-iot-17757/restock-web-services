package com.uitopic.restock.platform.devices.domain.services;

import com.uitopic.restock.platform.devices.domain.model.aggregates.DeviceHealth;
import com.uitopic.restock.platform.devices.domain.model.queries.GetDeviceHealthLogsByDeviceIdQuery;

import java.util.List;

/**
 * Service interface for handling queries related to device health logs.
 */
public interface DeviceHealthQueryService {

    /**
     * Handles the query to retrieve device health logs.
     *
     * @param query the query containing deviceId
     * @return list of device health records
     */
    List<DeviceHealth> handle(GetDeviceHealthLogsByDeviceIdQuery query);
}
