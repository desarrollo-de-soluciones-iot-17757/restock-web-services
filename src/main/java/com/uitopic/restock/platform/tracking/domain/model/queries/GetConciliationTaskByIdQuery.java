package com.uitopic.restock.platform.tracking.domain.model.queries;

/**
 * Query to get a conciliation task by its identifier.
 *
 * @param conciliationTaskId conciliation task identifier
 */
public record GetConciliationTaskByIdQuery(
        String conciliationTaskId
) {
    public GetConciliationTaskByIdQuery {
        if (conciliationTaskId == null || conciliationTaskId.isBlank()) {
            throw new IllegalArgumentException("Conciliation task ID cannot be null or blank");
        }
    }
}
