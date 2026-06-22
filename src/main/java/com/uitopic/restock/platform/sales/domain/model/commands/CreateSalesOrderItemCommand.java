package com.uitopic.restock.platform.sales.domain.model.commands;

import java.math.BigDecimal;

public record CreateSalesOrderItemCommand(
        String productId,
        String productType,
        String nameSnapshot,
        BigDecimal unitPrice,
        String currency,
        int quantity
) {
}
