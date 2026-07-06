package com.uitopic.restock.platform.devices.interfaces.rest.resources;

import java.util.Date;

public record DeviceHealthResource(
        String id,
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
