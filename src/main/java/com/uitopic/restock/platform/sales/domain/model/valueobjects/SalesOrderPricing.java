package com.uitopic.restock.platform.sales.domain.model.valueobjects;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.Money;

public record SalesOrderPricing(Money subtotal, Money taxes, Money total) {

    public SalesOrderPricing {
        if (subtotal == null || taxes == null || total == null) {
            throw new IllegalArgumentException("Pricing values cannot be null");
        }
    }
}
