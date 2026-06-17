package com.uitopic.restock.platform.analytics.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/** Represents the discrepancy between physical and recorded stock for a product. */
@Schema(name = "StockDiscrepancyResource", description = "Represents the discrepancy between physical and recorded stock for a product.")
public record StockDiscrepancyResource(

        /** Discrepancy unique identifier. */
        @Schema(description = "Discrepancy unique identifier")
        String discrepancyId,

        /** Physical stock recorded. */
        @Schema(description = "Physical stock recorded")
        Double physicalStock,

        /** System / digital stock recorded. */
        @Schema(description = "System / digital stock recorded")
        Double systemStock,

        /** Calculated difference between system and physical stock. */
        @Schema(description = "Calculated difference between system and physical stock")
        Double difference,

        /** Risk level of the discrepancy (LOW, MEDIUM, HIGH). */
        @Schema(description = "Risk level of the discrepancy (LOW, MEDIUM, HIGH)")
        String riskLevel,

        /** Status of the discrepancy (RESOLVED, UNRESOLVED). */
        @Schema(description = "Status of the discrepancy (RESOLVED, UNRESOLVED)")
        String status,

        /** Indicates whether the stock is conciliated (no discrepancy). */
        @Schema(description = "Indicates whether the stock is conciliated (no discrepancy)")
        Boolean isConciliated
) {
}
