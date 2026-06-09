package com.uitopic.restock.platform.resources.domain.model.queries;

/**
 * Query to get all batches from a custom supply.
 */
public record GetBatchesByCustomSupplyIdQuery(
        String customSupplyId
) {
    public GetBatchesByCustomSupplyIdQuery {
        if (customSupplyId == null || customSupplyId.isBlank()) {
            throw new IllegalArgumentException("Custom supply ID cannot be null or blank");
        }
    }
}