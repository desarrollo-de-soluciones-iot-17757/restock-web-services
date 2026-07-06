package com.uitopic.restock.platform.devices.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Request body to update the display mode for a device.")
public record UpdateDisplayModeResource(

        @Schema(description = "Target display mode")
        @NotBlank(message = "Display mode is required")
        @Pattern(
                regexp = "^(DISPLAY_MODE_ENVIRONMENT|DISPLAY_MODE_TEMPERATURE|DISPLAY_MODE_HUMIDITY|DISPLAY_MODE_WEIGHT|DISPLAY_MODE_CONVERTED_UNITS)$",
                message = "Display mode must be a valid display mode"
        )
        String displayMode
) {
}
