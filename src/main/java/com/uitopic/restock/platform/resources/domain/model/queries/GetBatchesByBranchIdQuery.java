package com.uitopic.restock.platform.resources.domain.model.queries;

/**
 * Query to get all batches from a branch.
 */
public record GetBatchesByBranchIdQuery(
        String branchId
) {
    public GetBatchesByBranchIdQuery {
        if (branchId == null || branchId.isBlank()) {
            throw new IllegalArgumentException("Branch ID cannot be null or blank");
        }
    }
}