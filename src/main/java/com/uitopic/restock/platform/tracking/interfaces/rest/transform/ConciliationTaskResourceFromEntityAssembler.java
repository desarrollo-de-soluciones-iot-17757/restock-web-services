package com.uitopic.restock.platform.tracking.interfaces.rest.transform;

import com.uitopic.restock.platform.tracking.domain.model.aggregates.ConciliationTask;
import com.uitopic.restock.platform.tracking.interfaces.rest.resources.ConciliationTaskResource;

/**
 * Assembler responsible for converting conciliation task domain aggregates into
 * REST resources.
 */
public final class ConciliationTaskResourceFromEntityAssembler {
    private ConciliationTaskResourceFromEntityAssembler() {
        // Utility class
    }

    /**
     * Converts a conciliation task aggregate into a response resource.
     *
     * @param task conciliation task domain aggregate
     * @return conciliation task resource with the comparison and resolution
     *         snapshot
     */
    public static ConciliationTaskResource toResourceFromEntity(ConciliationTask task) {
        return new ConciliationTaskResource(
                task.getId(),
                task.getDiscrepancyId(),
                task.getStockComparisonTaskId(),
                task.getAccountId() != null ? task.getAccountId().getAccountId() : null,
                task.getBranchId() != null ? task.getBranchId().getBranchId() : null,
                task.getBatchId() != null ? task.getBatchId().getBatchId() : null,
                task.getDeviceId() != null ? task.getDeviceId().getDeviceId() : null,
                task.getCustomSupplyId(),
                task.getCustomSupplyName(),
                task.getSystemStock() != null ? task.getSystemStock().getStock() : null,
                task.getPhysicalStock() != null ? task.getPhysicalStock().getStock() : null,
                task.getJustifiedWithdrawnStockUsed(),
                task.getTotalPhysicalStock(),
                task.getDifference(),
                task.getAlertLevel() != null ? task.getAlertLevel().name() : null,
                task.getConciliationStatus() != null ? task.getConciliationStatus().name() : null,
                task.getResolutionAction() != null ? task.getResolutionAction().name() : null,
                task.getResolutionReason() != null ? task.getResolutionReason().name() : null,
                task.getResolutionJustification(),
                task.getResolvedByUserId() != null ? task.getResolvedByUserId().userId() : null,
                task.getResolvedAt() != null ? task.getResolvedAt().toString() : null
        );
    }
}
