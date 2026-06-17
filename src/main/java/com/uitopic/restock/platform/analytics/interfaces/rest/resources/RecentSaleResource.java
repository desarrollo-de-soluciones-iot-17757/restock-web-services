package com.uitopic.restock.platform.analytics.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

/** Represents a recent sale transaction for analytics. */
@Schema(name = "RecentSaleResource", description = "Represents a recent sale transaction for analytics.")
public record RecentSaleResource(

        /** Sale unique identifier. */
        @Schema(description = "Sale unique identifier")
        String saleId,

        /** Branch identifier where the sale occurred. */
        @Schema(description = "Branch identifier where the sale occurred")
        String branchId,

        /** Total sale amount. */
        @Schema(description = "Total sale amount")
        Double totalAmount,

        /** Date when the sale was made. */
        @Schema(description = "Date when the sale was made")
        LocalDate saleDate,

        /** Sale status. */
        @Schema(description = "Sale status")
        String status
) {
}
