package com.uitopic.restock.platform.resources.domain.model.queries;

/**
 * Query to get a batch by its identifier.
 */
public record GetBatchByIdQuery(
        String batchId
) {
    public GetBatchByIdQuery {
        if (batchId == null || batchId.isBlank()) {
            throw new IllegalArgumentException("Batch ID cannot be null or blank");
        }
    }
}