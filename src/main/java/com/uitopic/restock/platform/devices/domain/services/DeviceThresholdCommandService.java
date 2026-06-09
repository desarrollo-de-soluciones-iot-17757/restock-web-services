package com.uitopic.restock.platform.devices.domain.services;

import com.uitopic.restock.platform.devices.domain.model.commands.CreateDeviceThresholdCommand;
import com.uitopic.restock.platform.devices.domain.model.entities.DeviceThreshold;

public interface DeviceThresholdCommandService {

    DeviceThreshold handle(CreateDeviceThresholdCommand command);
}
