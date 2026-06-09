package com.uitopic.restock.platform.devices.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Request body to transition device lifecycle status.")
public record UpdateDeviceStatusResource(

        @Schema(description = "Target status: CONFIGURED (completes onboarding) or INACTIVE (deactivates device)")
        @NotBlank(message = "Status is required")
        @Pattern(regexp = "^(CONFIGURED|INACTIVE)$", message = "Status must be CONFIGURED or INACTIVE")
        String status
) {
}
