package com.uitopic.restock.platform.analytics.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/** Represents a product whose stock is below the configured minimum threshold. */
@Schema(name = "CriticalProductResource", description = "Represents a product whose stock is below the configured minimum threshold.")
public record CriticalProductResource(

        /** Product (custom supply) unique identifier. */
        @Schema(description = "Product (custom supply) unique identifier")
        String productId,

        /** Product display name. */
        @Schema(description = "Product display name")
        String productName,

        /** Base supply identifier. */
        @Schema(description = "Base supply identifier")
        String supplyId,

        /** Total current stock across all batches in the branch. */
        @Schema(description = "Total current stock across all batches in the branch")
        Double totalStock,

        /** Minimum stock threshold configured for this product. */
        @Schema(description = "Minimum stock threshold configured for this product")
        Double minStock,

        /** Maximum stock threshold configured for this product. */
        @Schema(description = "Maximum stock threshold configured for this product")
        Double maxStock,

        /** Stock deficit (how much below minStock, 0 if above or equal). */
        @Schema(description = "Stock deficit (how much below minStock, 0 if above or equal)")
        Double stockDeficit,

        /** Branch name where the stock is located. */
        @Schema(description = "Branch name where the stock is located")
        String branchName,

        /** Branch identifier. */
        @Schema(description = "Branch identifier")
        String branchId,

        /** Unit of measurement. */
        @Schema(description = "Unit of measurement")
        String unitMeasurement
) {
}
