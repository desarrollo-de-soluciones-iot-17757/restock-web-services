package com.uitopic.restock.platform.tracking.interfaces.rest.transform;

import com.uitopic.restock.platform.tracking.domain.model.entities.Discrepancy;
import com.uitopic.restock.platform.tracking.interfaces.rest.resources.DiscrepancyResource;

/**
 * Assembler responsible for converting discrepancy domain entities into REST
 * resources.
 */
public final class DiscrepancyResourceFromEntityAssembler {
    private DiscrepancyResourceFromEntityAssembler() {
        // Utility class
    }

    /**
     * Converts a discrepancy entity into a response resource.
     *
     * @param discrepancy discrepancy domain entity
     * @return discrepancy resource
     */
    public static DiscrepancyResource toResourceFromEntity(Discrepancy discrepancy) {
        return new DiscrepancyResource(
                discrepancy.getId(),
                discrepancy.getStockComparisonTaskId(),
                discrepancy.getAccountId() != null ? discrepancy.getAccountId().getAccountId() : null,
                discrepancy.getBranchId() != null ? discrepancy.getBranchId().getBranchId() : null,
                discrepancy.getBatchId() != null ? discrepancy.getBatchId().getBatchId() : null,
                discrepancy.getDeviceId() != null ? discrepancy.getDeviceId().getDeviceId() : null,
                discrepancy.getCustomSupplyId(),
                discrepancy.getCustomSupplyName(),
                discrepancy.getSystemStock() != null ? discrepancy.getSystemStock().getStock() : null,
                discrepancy.getPhysicalStock() != null ? discrepancy.getPhysicalStock().getStock() : null,
                discrepancy.getJustifiedWithdrawnStockUsed(),
                discrepancy.getTotalPhysicalStock(),
                discrepancy.getQuantityDifference(),
                discrepancy.getRiskLevel() != null ? discrepancy.getRiskLevel().name() : null,
                discrepancy.getStatus() != null ? discrepancy.getStatus().name() : null,
                discrepancy.getCreatedAt() != null ? discrepancy.getCreatedAt().toString() : null,
                discrepancy.getResolvedAt() != null ? discrepancy.getResolvedAt().toString() : null
        );
    }
}
