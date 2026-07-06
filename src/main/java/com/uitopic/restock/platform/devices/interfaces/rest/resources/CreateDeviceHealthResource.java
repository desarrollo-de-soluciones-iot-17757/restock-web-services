package com.uitopic.restock.platform.devices.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

@Schema(description = "Resource representing a device health / microcontroller status reading payload")
public record CreateDeviceHealthResource(

        @Schema(description = "Device ID", example = "00:00:00:00:00:00")
        @NotBlank
        String deviceId,

        @Schema(description = "Branch ID", example = "branch-001")
        String branchId,

        @Schema(description = "Alert type", example = "HEALTH_ANOMALY")
        String alertType,

        @Schema(description = "Metric name", example = "voltage")
        String metric,

        @Schema(description = "Current reading value as string", example = "3.10")
        String value,

        @Schema(description = "Configured threshold breached", example = "3.30")
        String threshold,

        @Schema(description = "Human readable message", example = "Voltage drop detected")
        String message,

        @Schema(description = "CPU usage percentage", example = "12.5")
        Double cpuUsagePercentage,

        @Schema(description = "Free memory bytes", example = "184320")
        Long memoryFreeBytes,

        @Schema(description = "Supply voltage in volts", example = "3.10")
        Double voltageVolts,

        @Schema(description = "Microcontroller temperature in Celsius", example = "42.0")
        Double temperatureInCelsius,

        @Schema(description = "Reading timestamp")
        Date timestamp
) {
}
