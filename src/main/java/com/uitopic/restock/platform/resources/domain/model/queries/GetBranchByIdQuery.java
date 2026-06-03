package com.uitopic.restock.platform.resources.domain.model.queries;

/**
 * Query to get a branch by its identifier.
 */
public record GetBranchByIdQuery(
        String branchId
) {
    public GetBranchByIdQuery {
        if (branchId == null || branchId.isBlank()) {
            throw new IllegalArgumentException("Branch ID cannot be null or blank");
        }
    }
}