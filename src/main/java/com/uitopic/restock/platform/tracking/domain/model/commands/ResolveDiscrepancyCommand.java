package com.uitopic.restock.platform.tracking.domain.model.commands;

/**
 * Command used to resolve an existing discrepancy.
 *
 * @param discrepancyId discrepancy identifier
 */
public record ResolveDiscrepancyCommand(String discrepancyId) {
    public ResolveDiscrepancyCommand {
        if (discrepancyId == null || discrepancyId.isBlank()) {
            throw new IllegalArgumentException("Discrepancy ID cannot be null or blank");
        }
    }
}
