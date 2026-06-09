package com.uitopic.restock.platform.devices.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request body to assign a batch to a device for monitoring.")
public record AssignBatchResource(

        @Schema(description = "Batch identifier to monitor")
        @NotBlank(message = "Batch ID is required")
        String batchId
) {
}
