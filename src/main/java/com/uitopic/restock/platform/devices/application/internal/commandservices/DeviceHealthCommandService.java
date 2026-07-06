package com.uitopic.restock.platform.devices.application.internal.commandservices;

import com.uitopic.restock.platform.devices.domain.model.aggregates.DeviceHealth;
import com.uitopic.restock.platform.devices.domain.model.commands.RegisterDeviceHealthCommand;

/**
 * Service interface for handling device health commands.
 */
public interface DeviceHealthCommandService {

    DeviceHealth handle(RegisterDeviceHealthCommand command);
}
