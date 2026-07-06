package com.uitopic.restock.platform.sales.domain.services;

import com.uitopic.restock.platform.sales.domain.model.aggregates.SalesOrder;
import com.uitopic.restock.platform.sales.domain.model.commands.*;

import java.util.Optional;

/**
 * Command service contract for SalesOrder write operations.
 */
public interface SalesOrderCommandService {

    /**
     * Creates a new sales order.
     *
     * @param command the create sales order command
     * @return the created sales order
     */
    SalesOrder handle(CreateSalesOrderCommand command);

    /**
     * Adds a product to an existing sales order.
     *
     * @param command the add product to order command
     * @return the updated sales order
     */
    SalesOrder handle(AddProductToOrderCommand command);

    /**
     * Removes a product from a sales order.
     *
     * @param command the remove product from order command
     * @return the updated sales order
     */
    SalesOrder handle(RemoveProductFromOrderCommand command);

    /**
     * Completes and finalizes a sales order.
     *
     * @param command command with the sales order identifier to complete
     */
    SalesOrder handle(CompleteSalesOrderCommand command);

    /**
     * Cancels an existing sales order.
     *
     * @param command command with the sales order identifier to cancel
     */
    void handle(CancelSalesOrderCommand command);
}
