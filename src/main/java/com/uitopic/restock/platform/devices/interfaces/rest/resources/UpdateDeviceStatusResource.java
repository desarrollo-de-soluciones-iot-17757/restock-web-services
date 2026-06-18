package com.uitopic.restock.platform.devices.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Request body to transition device lifecycle status.")
public record UpdateDeviceStatusResource(

        @Schema(description = "Target status: CALIBRATED (completes onboarding) or INACTIVE (deactivates device)")
        @NotBlank(message = "Status is required")
        @Pattern(regexp = "^(CALIBRATED|INACTIVE)$", message = "Status must be CALIBRATED or INACTIVE")
        String status
) {
}
