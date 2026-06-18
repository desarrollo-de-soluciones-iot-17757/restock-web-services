package com.uitopic.restock.platform.sales.domain.services;

import com.uitopic.restock.platform.sales.domain.model.aggregates.SalesOrder;
import com.uitopic.restock.platform.sales.domain.model.commands.CreateSalesOrderCommand;

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
}
