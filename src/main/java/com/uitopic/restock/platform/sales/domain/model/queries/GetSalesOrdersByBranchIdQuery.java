package com.uitopic.restock.platform.sales.domain.model.queries;

/**
 * Query to get sales orders by branch with optional filters.
 */
public record GetSalesOrdersByBranchIdQuery(
        String branchId
) {

    /**
     * Constructs a GetSalesOrdersByBranchQuery with validation.
     */
    public GetSalesOrdersByBranchIdQuery {
        if (branchId == null || branchId.isBlank()) {
            throw new IllegalArgumentException("Branch ID cannot be null or blank");
        }
    }
}

