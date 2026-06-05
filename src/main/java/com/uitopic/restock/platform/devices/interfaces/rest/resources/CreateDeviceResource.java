package com.uitopic.restock.platform.devices.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Request body to register a new IoT device.")
public record CreateDeviceResource(

        @Schema(description = "Account (business owner) ID for this device")
        @NotBlank(message = "Account ID is required")
        String accountId,

        @Schema(description = "MAC address of the device (format: XX:XX:XX:XX:XX:XX)")
        @NotBlank(message = "MAC address is required")
        @Pattern(regexp = "^([0-9A-Fa-f]{2}:){5}[0-9A-Fa-f]{2}$", message = "Invalid MAC address format")
        String macAddress,

        @Schema(description = "Human-readable description of the device")
        String description
) {
}
