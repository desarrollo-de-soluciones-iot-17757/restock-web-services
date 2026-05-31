package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/** Request resource for subtracting inventory stock within the resources bounded context. */
@Schema(description = "Request resource for subtracting inventory stock")
public record CreateInventorySubtractResource(
        @NotBlank @Schema(description = "Branch ID") String branchId,
        @NotBlank @Schema(description = "Custom supply ID") String customSupplyId,
        @Min(0) @Schema(description = "Quantity to subtract") double quantity,
        @NotBlank @Schema(description = "Unit of measurement") String unit,
        @Schema(description = "Reason for subtraction") String reason,
        @Schema(description = "Timestamp (ISO format, defaults to now)") String timestamp
) {}
