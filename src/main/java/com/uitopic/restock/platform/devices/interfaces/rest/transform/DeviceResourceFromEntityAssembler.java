package com.uitopic.restock.platform.devices.interfaces.rest.transform;

import com.uitopic.restock.platform.devices.domain.model.aggregates.Device;
import com.uitopic.restock.platform.devices.interfaces.rest.resources.DeviceResource;

public class DeviceResourceFromEntityAssembler {

    private DeviceResourceFromEntityAssembler() {
        throw new IllegalStateException("Utility class");
    }

    public static DeviceResource toResourceFromEntity(Device device) {
        var specs = device.getSpecifications();
        var weight = device.getWeightMeasurement();

        return new DeviceResource(
                device.getId(),
                device.getAccountId() != null ? device.getAccountId().getAccountId() : null,
                device.getBranchId(),
                device.getAssignedBatchId(),
                device.getSupplyThresholdId(),
                device.getMacAddress() != null ? device.getMacAddress().address() : null,
                device.getDescription(),
                specs != null ? specs.manufacturer() : null,
                specs != null ? specs.model() : null,
                specs != null ? specs.firmwareVersion() : null,
                weight != null ? weight.unitStockWeight() : null,
                weight != null ? weight.tareWeight() : null,
                weight != null ? weight.grossWeight() : null,
                weight != null && weight.calibrationDate() != null ? weight.calibrationDate().toString() : null,
                weight != null && weight.weightUnit() != null ? weight.weightUnit().unitName() : null,
                weight != null && weight.weightUnit() != null ? weight.weightUnit().abbreviation() : null,
                device.getJustifiedWithdrawnStock(),
                device.getStatus() != null ? device.getStatus().name() : null
        );
    }
}
