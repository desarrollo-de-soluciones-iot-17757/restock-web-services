package com.uitopic.restock.platform.sales.domain.model.queries;

/**
 * Query to get a sales order by its identifier.
 */
public record GetSalesOrderByIdQuery(
        String orderId
) {
    /**
     * Constructs a GetSalesOrderByIdQuery with validation.
     */
    public GetSalesOrderByIdQuery {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("Order ID cannot be null or blank");
        }
    }
}

