package com.uitopic.restock.platform.devices.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request body to assign a device to a branch.")
public record AssignBranchResource(

        @Schema(description = "Branch identifier")
        @NotBlank(message = "Branch ID is required")
        String branchId
) {
}
