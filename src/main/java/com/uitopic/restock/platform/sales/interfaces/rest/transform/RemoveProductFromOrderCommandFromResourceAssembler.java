package com.uitopic.restock.platform.sales.interfaces.rest.transform;

import com.uitopic.restock.platform.sales.domain.model.commands.RemoveProductFromOrderCommand;

public class RemoveProductFromOrderCommandFromResourceAssembler {
    public static RemoveProductFromOrderCommand toCommandFromResource(String orderId, String itemId) {
        return new RemoveProductFromOrderCommand(orderId, itemId);
    }
}