package com.uitopic.restock.platform.devices.interfaces.rest.transform;

import com.uitopic.restock.platform.devices.domain.model.commands.RegisterDeviceHealthCommand;
import com.uitopic.restock.platform.devices.interfaces.rest.resources.CreateDeviceHealthResource;

public class CreateDeviceHealthCommandFromResourceAssembler {

    public static RegisterDeviceHealthCommand toCommandFromResource(CreateDeviceHealthResource resource) {
        return new RegisterDeviceHealthCommand(
                resource.deviceId(),
                resource.branchId(),
                resource.alertType(),
                resource.metric(),
                resource.value(),
                resource.threshold(),
                resource.message(),
                resource.cpuUsagePercentage(),
                resource.memoryFreeBytes(),
                resource.voltageVolts(),
                resource.temperatureInCelsius(),
                resource.timestamp()
        );
    }
}
