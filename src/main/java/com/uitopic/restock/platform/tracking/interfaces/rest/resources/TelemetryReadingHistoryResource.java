package com.uitopic.restock.platform.tracking.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

/**
 * Resource representing a historical telemetry reading, including physical stock, temperature, humidity, timestamp, and device ID.
 */
@Schema(
        name = "TelemetryReadingHistoryResource",
        description = "Resource representing a historical telemetry reading from a device, containing information about stock levels, temperature, humidity, timestamp, and the device ID."
)
public record TelemetryReadingHistoryResource(
        @Schema(description = "Unique identifier of the telemetry reading")
        String id,

        @Schema(description = "Physical stock level reported by the device", example = "150.0")
        Double physicalStock,

        @Schema(description = "Temperature in Celsius reported by the device", example = "22.5")
        Double temperature,

        @Schema(description = "Humidity percentage reported by the device", example = "45.0")
        Double humidity,

        @Schema(description = "Timestamp when the telemetry reading was recorded")
        Instant timestamp,

        @Schema(description = "Unique identifier of the device", example = "device-001")
        String deviceId
) {
}
