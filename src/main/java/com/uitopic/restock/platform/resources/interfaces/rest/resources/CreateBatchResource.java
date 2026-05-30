package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request resource for creating a batch")
public record CreateBatchResource(
        @NotBlank @Schema(description = "Account ID") String accountId,
        @NotBlank @Schema(description = "Branch ID") String branchId,
        @NotBlank @Schema(description = "Custom supply ID") String customSupplyId,
        @Min(0) @Schema(description = "Current quantity") double currentQuantity,
        @NotBlank @Schema(description = "Unit of measurement") String unit,
        @Schema(description = "Expiration date (ISO format)") String expirationDate
) {}
