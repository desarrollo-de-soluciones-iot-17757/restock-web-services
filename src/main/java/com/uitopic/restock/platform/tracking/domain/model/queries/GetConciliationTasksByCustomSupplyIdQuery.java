package com.uitopic.restock.platform.tracking.domain.model.queries;

/**
 * Query to get conciliation tasks by custom supply identifier.
 *
 * @param customSupplyId custom supply identifier
 */
public record GetConciliationTasksByCustomSupplyIdQuery(
        String customSupplyId
) {
    public GetConciliationTasksByCustomSupplyIdQuery {
        if (customSupplyId == null || customSupplyId.isBlank()) {
            throw new IllegalArgumentException("Custom supply ID cannot be null or blank");
        }
    }
}
