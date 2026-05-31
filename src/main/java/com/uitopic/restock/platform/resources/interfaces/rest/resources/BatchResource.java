package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/** Response resource representing a batch within the resources bounded context. */
@Schema(description = "Response resource representing a batch")
public record BatchResource(
        @Schema(description = "Unique identifier of the batch") String id,
        @Schema(description = "Account ID") String accountId,
        @Schema(description = "Branch ID") String branchId,
        @Schema(description = "Custom supply ID") String customSupplyId,
        @Schema(description = "Current quantity in this batch") double currentQuantity,
        @Schema(description = "Unit of measurement") String unit,
        @Schema(description = "Expiration date (ISO format)") String expirationDate,
        @Schema(description = "Creation timestamp") String createdAt
) {}
