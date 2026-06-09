package com.uitopic.restock.platform.devices.domain.services;

import com.uitopic.restock.platform.devices.domain.model.aggregates.Device;
import com.uitopic.restock.platform.devices.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface DeviceQueryService {

    Optional<Device> handle(GetDeviceByIdQuery query);

    Optional<Device> handle(GetDeviceByMacAddressQuery query);

    List<Device> handle(GetDevicesByAccountIdQuery query);
}
