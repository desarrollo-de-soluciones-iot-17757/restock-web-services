package com.uitopic.restock.platform.devices.domain.model.aggregates;

import com.uitopic.restock.platform.devices.domain.model.commands.RegisterDeviceHealthCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;

/**
 * DeviceHealth Aggregate representing a device or microcontroller health metric reading.
 */
@Getter
@NoArgsConstructor
public class DeviceHealth {

    private String id;
    private String deviceId;
    private String branchId;
    private String alertType;
    private String metric;
    private String value;
    private String threshold;
    private String message;
    private Double cpuUsagePercentage;
    private Long memoryFreeBytes;
    private Double voltageVolts;
    private Double temperatureInCelsius;
    private Date timestamp;

    public DeviceHealth(RegisterDeviceHealthCommand command) {
        this.deviceId = command.deviceId();
        this.branchId = command.branchId();
        this.alertType = command.alertType();
        this.metric = command.metric();
        this.value = command.value();
        this.threshold = command.threshold();
        this.message = command.message();
        this.cpuUsagePercentage = command.cpuUsagePercentage();
        this.memoryFreeBytes = command.memoryFreeBytes();
        this.voltageVolts = command.voltageVolts();
        this.temperatureInCelsius = command.temperatureInCelsius();
        this.timestamp = command.timestamp() != null ? command.timestamp() : Date.from(Instant.now());
    }

    public DeviceHealth(
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
        this.id = id;
        this.deviceId = deviceId;
        this.branchId = branchId;
        this.alertType = alertType;
        this.metric = metric;
        this.value = value;
        this.threshold = threshold;
        this.message = message;
        this.cpuUsagePercentage = cpuUsagePercentage;
        this.memoryFreeBytes = memoryFreeBytes;
        this.voltageVolts = voltageVolts;
        this.temperatureInCelsius = temperatureInCelsius;
        this.timestamp = timestamp;
    }
}
