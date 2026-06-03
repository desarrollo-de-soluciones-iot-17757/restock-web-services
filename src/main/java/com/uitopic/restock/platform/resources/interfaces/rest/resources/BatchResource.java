package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response resource representing a batch.
 */
@Schema(description = "Response resource representing a batch")
public record BatchResource(
        @Schema(description = "Batch ID")
        String id,

        @Schema(description = "Batch code")
        String code,

        @Schema(description = "Current stock quantity")
        double currentStock,

        @Schema(description = "Unit of measurement")
        String unitMeasurement,

        @Schema(description = "Unit of measurement abbreviation")
        String unitMeasurementAbbreviation,

        @Schema(description = "Custom supply ID")
        String customSupplyId,

        @Schema(description = "Branch ID")
        String branchId,

        @Schema(description = "Account ID")
        String accountId,

        @Schema(description = "Expiration date")
        String expirationDate,

        @Schema(description = "Entry date")
        String entryDate
) {
}