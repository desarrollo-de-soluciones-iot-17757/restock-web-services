package com.uitopic.restock.platform.subscriptions.domain.model.entities;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.Money;
import com.uitopic.restock.platform.subscriptions.domain.model.valueobjects.BillingInterval;
import com.uitopic.restock.platform.subscriptions.domain.model.valueobjects.PlanLimits;
import com.uitopic.restock.platform.subscriptions.domain.model.valueobjects.PlanType;

import java.math.BigDecimal;
import java.util.List;

/**
 * Canonical commercial plan definitions offered by Restock.
 */
public final class SubscriptionPlanCatalog {
    private SubscriptionPlanCatalog() {
    }

    public static List<SubscriptionPlan> predefinedPlans() {
        return List.of(
                plan("Basic", PlanType.BASIC,
                        new PlanLimits(1, 3, 100, 30),
                        List.of("Centralize Inventory", "Stock Alerts", "Email Support"),
                        "29.00", "290.00"),
                plan("Pro", PlanType.PRO,
                        new PlanLimits(5, 15, 1000, 250),
                        List.of("All Basic", "Analytics", "Multiple branches"),
                        "79.00", "790.00"),
                plan("Premium", PlanType.PREMIUM,
                        new PlanLimits(20, 75, 10000, 2000),
                        List.of("All Pro", "Priority support", "More capacity"),
                        "199.00", "1990.00")
        );
    }

    private static SubscriptionPlan plan(
            String name,
            PlanType type,
            PlanLimits limits,
            List<String> benefits,
            String monthlyPrice,
            String yearlyPrice
    ) {
        return new SubscriptionPlan(
                null,
                name,
                "Plan " + name + " for businesses that use Restock.",
                type,
                null,
                limits,
                benefits,
                List.of(
                        new PlanPrice(BillingInterval.MONTHLY, money(monthlyPrice), null),
                        new PlanPrice(BillingInterval.YEARLY, money(yearlyPrice), null)
                ),
                true
        );
    }

    private static Money money(String amount) {
        return new Money(new BigDecimal(amount), "USD");
    }
}
