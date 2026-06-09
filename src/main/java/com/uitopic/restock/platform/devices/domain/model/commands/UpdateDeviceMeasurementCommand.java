package com.uitopic.restock.platform.devices.domain.model.commands;

import java.time.LocalDate;

public record UpdateDeviceMeasurementCommand(
        String deviceId,
        Double netWeight,
        Double tareWeight,
        Double grossWeight,
        LocalDate calibrationDate,
        String weightUnitName,
        String weightUnitAbbreviation
) {
    public UpdateDeviceMeasurementCommand {
        if (deviceId == null || deviceId.isBlank())
            throw new IllegalArgumentException("Device ID cannot be null or blank");
        if (netWeight == null || netWeight < 0)
            throw new IllegalArgumentException("Net weight cannot be null or negative");
        if (tareWeight == null || tareWeight < 0)
            throw new IllegalArgumentException("Tare weight cannot be null or negative");
        if (grossWeight == null || grossWeight < 0)
            throw new IllegalArgumentException("Gross weight cannot be null or negative");
        if (calibrationDate == null)
            throw new IllegalArgumentException("Calibration date cannot be null");
        if (weightUnitName == null || weightUnitName.isBlank())
            throw new IllegalArgumentException("Weight unit name cannot be null or blank");
    }
}
