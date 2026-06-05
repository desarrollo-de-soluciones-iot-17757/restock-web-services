package com.uitopic.restock.platform.devices.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request body to link a supply threshold to a device.")
public record AssignSupplyThresholdResource(

        @Schema(description = "Supply threshold identifier")
        @NotBlank(message = "Supply threshold ID is required")
        String supplyThresholdId
) {
}
