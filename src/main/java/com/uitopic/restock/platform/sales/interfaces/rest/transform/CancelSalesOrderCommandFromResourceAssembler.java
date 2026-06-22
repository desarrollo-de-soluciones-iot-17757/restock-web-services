package com.uitopic.restock.platform.sales.interfaces.rest.transform;

import com.uitopic.restock.platform.sales.domain.model.commands.CancelSalesOrderCommand;

public class CancelSalesOrderCommandFromResourceAssembler {
    public static CancelSalesOrderCommand toCommandFromResource(String orderId) {
        return new CancelSalesOrderCommand(orderId);
    }
}