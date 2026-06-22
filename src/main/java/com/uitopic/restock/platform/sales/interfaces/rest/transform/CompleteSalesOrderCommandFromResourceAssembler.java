package com.uitopic.restock.platform.sales.interfaces.rest.transform;

import com.uitopic.restock.platform.sales.domain.model.commands.CompleteSalesOrderCommand;

public class CompleteSalesOrderCommandFromResourceAssembler {
    public static CompleteSalesOrderCommand toCommandFromResource(String orderId) {
        return new CompleteSalesOrderCommand(orderId);
    }
}