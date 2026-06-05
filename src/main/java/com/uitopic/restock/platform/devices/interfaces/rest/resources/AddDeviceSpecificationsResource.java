package com.uitopic.restock.platform.devices.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request body to add technical specifications to a device.")
public record AddDeviceSpecificationsResource(

        @Schema(description = "Device manufacturer")
        @NotBlank(message = "Manufacturer is required")
        String manufacturer,

        @Schema(description = "Device model")
        @NotBlank(message = "Model is required")
        String model,

        @Schema(description = "Firmware version")
        @NotBlank(message = "Firmware version is required")
        String firmwareVersion
) {
}
