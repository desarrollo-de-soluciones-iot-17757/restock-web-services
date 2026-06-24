package com.uitopic.restock.platform.subscriptions.domain.model.entities;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.Money;
import com.uitopic.restock.platform.subscriptions.domain.model.valueobjects.BillingInterval;

import java.util.Objects;

public record PlanPrice(
        BillingInterval billingInterval,
        Money money,
        String stripePriceId
) {
    public PlanPrice {
        Objects.requireNonNull(billingInterval, "Billing interval is required");
        Objects.requireNonNull(money, "Price is required");
    }
}
