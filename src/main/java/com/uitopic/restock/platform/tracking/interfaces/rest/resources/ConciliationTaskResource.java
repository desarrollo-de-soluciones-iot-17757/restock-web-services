package com.uitopic.restock.platform.tracking.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response resource that exposes the full conciliation task snapshot.
 *
 * <p>
 * The stock fields are intentionally split into digital stock, device physical
 * stock, justified withdrawn stock, total physical stock and difference so the
 * frontend can explain the discrepancy to administrators.
 *
 * @param id conciliation task identifier
 * @param discrepancyId discrepancy associated with this task
 * @param stockComparisonTaskId stock comparison task that originated the
 *                              discrepancy
 * @param accountId account identifier
 * @param branchId branch identifier
 * @param batchId batch identifier
 * @param deviceId device identifier
 * @param customSupplyId custom supply identifier
 * @param customSupplyName custom supply display name
 * @param digitalStock stock registered in the system
 * @param devicePhysicalStock physical stock reported by the device
 * @param justifiedWithdrawnStock physical stock outside the device but
 *                                justified operationally
 * @param totalPhysicalStock sum of device physical stock and justified
 *                           withdrawn stock
 * @param difference absolute difference between digital and total physical
 *                   stock
 * @param alertLevel discrepancy alert level
 * @param status current conciliation task status
 * @param resolutionAction action used to resolve the task, when resolved
 * @param resolutionReason reason used to resolve the task, when resolved
 * @param resolutionJustification administrator-provided justification, when
 *                                resolved
 * @param resolvedByUserId user that resolved the task manually
 * @param resolvedAt timestamp when the task was resolved
 */
@Schema(
        name = "ConciliationTaskResource",
        description = "Resource representing a conciliation task in the tracking system."
)
public record ConciliationTaskResource(
        String id,
        String discrepancyId,
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
        String alertLevel,
        String status,
        String resolutionAction,
        String resolutionReason,
        String resolutionJustification,
        String resolvedByUserId,
        String resolvedAt
) {
}
