package com.uitopic.restock.platform.devices.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "DeviceThresholdResource", description = "Response resource representing a device supply threshold.")
public record DeviceThresholdResource(

        @Schema(description = "Threshold unique identifier")
        String id,

        @Schema(description = "Account that owns this threshold")
        String accountId,

        @Schema(description = "Custom supply this threshold applies to")
        String customSupplyId,

        @Schema(description = "Minimum stock level")
        Double minStock,

        @Schema(description = "Maximum stock level")
        Double maxStock,

        @Schema(description = "Acceptable stock discrepancy before anomaly alert fires")
        Double anomalyThreshold,

        @Schema(description = "Minimum acceptable temperature in Celsius")
        Double minTemperatureCelsius,

        @Schema(description = "Maximum acceptable temperature in Celsius")
        Double maxTemperatureCelsius,

        @Schema(description = "Minimum acceptable humidity percentage")
        Double minHumidityPercentage,

        @Schema(description = "Maximum acceptable humidity percentage")
        Double maxHumidityPercentage
) {
}
