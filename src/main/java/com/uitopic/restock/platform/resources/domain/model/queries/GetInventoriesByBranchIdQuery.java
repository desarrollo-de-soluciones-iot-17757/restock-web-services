package com.uitopic.restock.platform.resources.domain.model.queries;

/**
 * Query to get inventories by branch ID.
 *
 * @param branchId the ID of the branch to retrieve inventories for
 */
public record GetInventoriesByBranchIdQuery(String branchId) {

    public GetInventoriesByBranchIdQuery {
        if (branchId == null || branchId.isBlank()) {
            throw new IllegalArgumentException("Branch ID cannot be null or blank");
        }
    }
}
