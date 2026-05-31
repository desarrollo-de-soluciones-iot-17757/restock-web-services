package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/** Response resource representing a batch within the resources bounded context. */
@Schema(description = "Response resource representing a batch")
public record BatchResource(
        @Schema(description = "Unique identifier of the batch") String id,
        @Schema(description = "Unique code of the batch") String code,
        @Schema(description = "Initial quantity in this batch") double initialStock,
        @Schema(description = "Current quantity in this batch") double currentStock,
        @Schema(description = "Unit of measurement of the stock of this batch") String unitMeasurement,
        @Schema(description = "Amount of purchase per unit") double unitPurchaseCostAmount,
        @Schema(description = "Currency of cost per unit purchased") String unitPurchaseCostCurrency,
        @Schema(description = "Custom supply ID") String customSupplyId,
        @Schema(description = "Branch ID") String branchId,
        @Schema(description = "Account ID") String accountId,
        @Schema(description = "Manufacturing Date (ISO format)") String manufacturingDate,
        @Schema(description = "Expiration date (ISO format)") String expirationDate,
        @Schema(description = "Entry date (ISO format)") String entryDate,
        @Schema(description = "Creation timestamp") String createdAt
) {}
