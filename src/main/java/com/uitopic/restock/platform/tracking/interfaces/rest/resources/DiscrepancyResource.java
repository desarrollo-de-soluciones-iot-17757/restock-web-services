package com.uitopic.restock.platform.tracking.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response resource exposing a discrepancy snapshot.
 */
@Schema(
        name = "DiscrepancyResource",
        description = "Resource representing a stock discrepancy detected by the tracking system."
)
public record DiscrepancyResource(
        String id,
        String stockComparisonTaskId,
        String accountId,
        String branchId,
        String batchId,
        String deviceId,
        String customSupplyId,
        String customSupplyName,
        Double digitalStock,
        Double devicePhysicalStock,
        Double justifiedWithdrawnStock,
        Double totalPhysicalStock,
        Double difference,
        String riskLevel,
        String status,
        String createdAt,
        String resolvedAt
) {
}
