package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/** Request resource for transferring inventory between branches within the resources bounded context. */
@Schema(description = "Request resource for transferring inventory between branches")
public record CreateInventoryTransferResource(
        @NotBlank @Schema(description = "Source branch ID") String fromBranchId,
        @NotBlank @Schema(description = "Destination branch ID") String toBranchId,
        @NotBlank @Schema(description = "Custom supply ID") String customSupplyId,
        @Min(0) @Schema(description = "Quantity to transfer") double quantity,
        @NotBlank @Schema(description = "Unit of measurement") String unit,
        @Schema(description = "Reason for transfer") String reason
) {}
