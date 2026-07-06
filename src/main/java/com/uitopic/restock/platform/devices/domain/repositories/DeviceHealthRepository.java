package com.uitopic.restock.platform.devices.domain.repositories;

import com.uitopic.restock.platform.devices.domain.model.aggregates.DeviceHealth;

import java.util.List;

/**
 * Domain repository interface for DeviceHealth aggregates.
 */
public interface DeviceHealthRepository {

    DeviceHealth save(DeviceHealth deviceHealth);

    List<DeviceHealth> findByDeviceId(String deviceId);
}
