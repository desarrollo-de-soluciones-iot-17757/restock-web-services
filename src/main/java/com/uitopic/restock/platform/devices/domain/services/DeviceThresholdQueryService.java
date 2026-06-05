package com.uitopic.restock.platform.devices.domain.services;

import com.uitopic.restock.platform.devices.domain.model.entities.DeviceThreshold;
import com.uitopic.restock.platform.devices.domain.model.queries.GetDeviceThresholdByIdQuery;
import com.uitopic.restock.platform.devices.domain.model.queries.GetDeviceThresholdsByAccountIdQuery;

import java.util.List;
import java.util.Optional;

public interface DeviceThresholdQueryService {

    Optional<DeviceThreshold> handle(GetDeviceThresholdByIdQuery query);

    List<DeviceThreshold> handle(GetDeviceThresholdsByAccountIdQuery query);
}
