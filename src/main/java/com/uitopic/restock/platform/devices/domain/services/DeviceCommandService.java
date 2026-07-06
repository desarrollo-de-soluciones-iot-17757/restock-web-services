package com.uitopic.restock.platform.devices.domain.services;

import com.uitopic.restock.platform.devices.domain.model.aggregates.Device;
import com.uitopic.restock.platform.devices.domain.model.commands.*;

import java.util.Optional;

public interface DeviceCommandService {

    Device handle(RegisterDeviceCommand command);

    Optional<Device> handle(AddDeviceSpecificationsCommand command);

    Optional<Device> handle(AssignDeviceToBranchCommand command);

    Optional<Device> handle(AssignBatchToDeviceCommand command);

    Optional<Device> handle(AssignSupplyThresholdCommand command);

    Optional<Device> handle(UpdateDeviceMeasurementCommand command);

    Optional<Device> handle(ConfirmDeviceConfigurationCommand command);

    Optional<Device> handle(UpdateJustifiedWithdrawnStockCommand command);

    Optional<Device> handle(DeactivateDeviceCommand command);

    Optional<Device> handle(UpdateDeviceDisplayModeCommand command);
}
