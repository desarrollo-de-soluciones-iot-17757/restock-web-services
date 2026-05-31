package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request resource for creating a batch")
public record CreateBatchResource(
        @NotBlank @Schema(description = "Batch code")
        String code,
        @Min(0) @Schema(description = "Current quantity")
        Double initialStock,
        @NotBlank @Schema(description = "Unit of measurement (e.g., 'kg', 'liters', 'units')")
        String unitMeasurement,
        @NotBlank @Schema(description = "Unit purchase cost amount")
        String unitPurchaseCostAmount,
        @NotBlank @Schema(description = "Unit purchase cost currency")
        String unitPurchaseCostCurrency,
        @NotBlank @Schema(description = "Custom supply ID")
        String customSupplyId,
        @NotBlank @Schema(description = "Branch ID")
        String receivingBranchId,
        @NotBlank @Schema(description = "Account ID")
        String accountId,
        @Schema(description = "Manufacturing date (ISO format)")
        String manufacturingDate,
        @Schema(description = "Expiration date (ISO format)")
        String expirationDate,
        @Schema(description = "Entry date (ISO format)")
        String entryDate
) {}
