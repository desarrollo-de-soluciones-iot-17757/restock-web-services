package com.uitopic.restock.platform.devices.domain.model.commands;

import java.util.Date;

/**
 * Command to register a device health reading.
 */
public record RegisterDeviceHealthCommand(
        String deviceId,
        String branchId,
        String alertType,
        String metric,
        String value,
        String threshold,
        String message,
        Double cpuUsagePercentage,
        Long memoryFreeBytes,
        Double voltageVolts,
        Double temperatureInCelsius,
        Date timestamp
) {
}
