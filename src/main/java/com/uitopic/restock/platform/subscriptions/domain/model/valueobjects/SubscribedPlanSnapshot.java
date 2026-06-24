package com.uitopic.restock.platform.subscriptions.domain.model.valueobjects;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.Money;
import com.uitopic.restock.platform.subscriptions.domain.model.entities.PlanPrice;
import com.uitopic.restock.platform.subscriptions.domain.model.entities.SubscriptionPlan;

import java.util.List;

public record SubscribedPlanSnapshot(
        String planId,
        String code,
        String name,
        PlanType type,
        BillingInterval billingInterval,
        Money price,
        PlanLimits limits,
        List<String> benefits,
        String stripePriceId
) {
    public SubscribedPlanSnapshot {
        benefits = benefits == null ? List.of() : List.copyOf(benefits);
    }

    public static SubscribedPlanSnapshot from(SubscriptionPlan plan, BillingInterval interval) {
        if (!plan.active()) throw new IllegalStateException("Cannot subscribe to an inactive plan");

        PlanPrice selectedPrice = plan.prices().stream()
                .filter(price -> price.billingInterval() == interval)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Plan does not support " + interval));

        return new SubscribedPlanSnapshot(
                plan.id(), plan.code(), plan.name(), plan.type(), interval,
                selectedPrice.money(), plan.limits(), plan.benefits(), selectedPrice.stripePriceId()
        );
    }
}
