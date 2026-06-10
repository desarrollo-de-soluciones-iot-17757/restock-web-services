package com.uitopic.restock.platform.devices.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(description = "Request body to create a device supply threshold.")
public record CreateDeviceThresholdResource(

        @Schema(description = "Account (business owner) ID for this threshold")
        @NotBlank(message = "Account ID is required")
        String accountId,

        @Schema(description = "Custom supply this threshold applies to")
        @NotBlank(message = "Custom supply ID is required")
        String customSupplyId,

        @Schema(description = "Device ID this threshold applies to")
        @NotBlank(message = "Device ID is required")
        String deviceId,

        @Schema(description = "Minimum stock level — triggers low-stock alert when breached")
        @NotNull(message = "Min stock is required")
        @PositiveOrZero(message = "Min stock must be zero or positive")
        Double minStock,

        @Schema(description = "Maximum expected stock level")
        @NotNull(message = "Max stock is required")
        @PositiveOrZero(message = "Max stock must be zero or positive")
        Double maxStock,

        @Schema(description = "Acceptable discrepancy between physical and digital stock — triggers anomaly alert when exceeded")
        @NotNull(message = "Anomaly threshold is required")
        @PositiveOrZero(message = "Anomaly threshold must be zero or positive")
        Double anomalyThreshold,

        @Schema(description = "Minimum acceptable temperature in Celsius (optional)")
        Double minTemperatureCelsius,

        @Schema(description = "Maximum acceptable temperature in Celsius (optional)")
        Double maxTemperatureCelsius,

        @Schema(description = "Minimum acceptable humidity percentage (optional)")
        Double minHumidityPercentage,

        @Schema(description = "Maximum acceptable humidity percentage (optional)")
        Double maxHumidityPercentage
) {
}
