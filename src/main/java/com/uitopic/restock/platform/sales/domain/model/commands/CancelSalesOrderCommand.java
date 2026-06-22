package com.uitopic.restock.platform.sales.domain.model.commands;

/**
 * Command to cancel a sales order.
 * Only PENDING orders can be cancelled.
 */
public record CancelSalesOrderCommand(
        String orderId
) {

    /**
     * Constructs a CancelSalesOrderCommand with validation.
     */
    public CancelSalesOrderCommand {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("Order ID cannot be null or blank");
        }
    }
}

