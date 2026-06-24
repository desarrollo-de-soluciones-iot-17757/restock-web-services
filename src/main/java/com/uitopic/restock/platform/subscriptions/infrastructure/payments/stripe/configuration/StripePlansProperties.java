package com.uitopic.restock.platform.subscriptions.infrastructure.payments.stripe.configuration;

import com.uitopic.restock.platform.subscriptions.domain.model.valueobjects.PlanType;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "stripe.plans")
public record StripePlansProperties(
        PlanReference basic,
        PlanReference pro,
        PlanReference premium
) {
    public record PlanReference(
            String productId,
            String monthlyPriceId,
            String yearlyPriceId
    ) {
    }

    public PlanReference referenceFor(PlanType planType) {
        return switch (planType) {
            case BASIC -> basic;
            case PRO -> pro;
            case PREMIUM -> premium;
        };
    }
}
