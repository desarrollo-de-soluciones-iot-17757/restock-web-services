package com.uitopic.restock.platform.tracking.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Resource representing a telemetry reading from a device, containing information about stock levels, temperature, humidity, and the device ID. This resource is used to track inventory discrepancies and monitor environmental conditions in real-time.
 *
 * @param physicalStock the physical stock level reported by the device, provided by the request
 * @param temperatureInCelsius the temperature in Celsius reported by the device, provided by the request
 * @param humidityPercentage the humidity percentage reported by the device, provided by the request
 * @param assignedBatchId the batch ID assigned to the telemetry reading, provided by the request
 * @param deviceId the unique identifier of the device that provided the telemetry reading, provided by the request
 */
@Schema(
        name = "TelemetryReadingResource",
        description = "Resource representing a telemetry reading from a device, containing information about stock levels, temperature, humidity, and the device ID. This resource is used to track inventory discrepancies and monitor environmental conditions in real-time."
)
public record TelemetryReadingResource(

        @Schema(
                description = "Physical stock level reported by the device, provided by the request",
                example = "150.0"
        )
        Double physicalStock,

        @Schema(
                description = "Temperature in Celsius reported by the device, provided by the request",
                example = "22.5"
        )
        Double temperatureInCelsius,

        @Schema(
                description = "Humidity percentage reported by the device, provided by the request",
                example = "45.0"
        )
        Double humidityPercentage,

        @Schema(
                description = "Batch ID assigned to the telemetry reading, provided by the request",
                example = "batch-123"
        )
        String assignedBatchId,

        @Schema(
                description = "Unique identifier of the device that provided the telemetry reading, provided by the request",
                example = "device-001"
        )
        String deviceId,

        @Schema(
                description = "Timestamp of the telemetry reading, provided by the request",
                example = "2024-06-15T14:30:00Z"
        )
        String timestamp
) {
}
