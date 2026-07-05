package com.uitopic.restock.platform.tracking.domain.model.queries;

/**
 * Query used to retrieve a discrepancy by its identifier.
 *
 * @param discrepancyId discrepancy identifier
 */
public record GetDiscrepancyByIdQuery(String discrepancyId) {
    public GetDiscrepancyByIdQuery {
        if (discrepancyId == null || discrepancyId.isBlank()) {
            throw new IllegalArgumentException("Discrepancy ID cannot be null or blank");
        }
    }
}
