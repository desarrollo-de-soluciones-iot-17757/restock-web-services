package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 * Request resource for transferring stock from a batch to another branch.
 */
@Schema(description = "Request resource for transferring batch stock")
public record TransferBatchStockResource(
        @NotBlank
        @Schema(description = "Target branch ID")
        String targetBranchId,

        @Positive
        @Schema(description = "Quantity to transfer")
        double quantity,

        @Schema(description = "Transfer reason")
        String reason
) {
}