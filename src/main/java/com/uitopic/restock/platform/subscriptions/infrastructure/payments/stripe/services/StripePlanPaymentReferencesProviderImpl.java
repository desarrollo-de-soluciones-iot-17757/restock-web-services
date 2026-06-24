package com.uitopic.restock.platform.subscriptions.infrastructure.payments.stripe.services;

import com.uitopic.restock.platform.subscriptions.application.internal.outboundservices.payments.PlanPaymentReferencesProvider;
import com.uitopic.restock.platform.subscriptions.domain.model.entities.SubscriptionPlan;
import com.uitopic.restock.platform.subscriptions.infrastructure.payments.stripe.configuration.StripePlansProperties;
import org.springframework.stereotype.Component;

@Component
public class StripePlanPaymentReferencesProviderImpl implements PlanPaymentReferencesProvider {
    private final StripePlansProperties properties;

    public StripePlanPaymentReferencesProviderImpl(StripePlansProperties properties) {
        this.properties = properties;
    }

    @Override
    public SubscriptionPlan attachReferences(SubscriptionPlan plan) {
        var reference = properties.referenceFor(plan.type());
        validate(plan.type().name().toLowerCase(), reference);
        return plan.withStripeReferences(
                reference.productId(),
                reference.monthlyPriceId(),
                reference.yearlyPriceId()
        );
    }

    private void validate(String planCode, StripePlansProperties.PlanReference reference) {
        if (reference == null) {
            throw new IllegalStateException("Missing Stripe configuration for plan: " + planCode);
        }
        requirePrefix(planCode, "product-id", reference.productId(), "prod_");
        requirePrefix(planCode, "monthly-price-id", reference.monthlyPriceId(), "price_");
        requirePrefix(planCode, "yearly-price-id", reference.yearlyPriceId(), "price_");
        if (reference.monthlyPriceId().equals(reference.yearlyPriceId())) {
            throw new IllegalStateException(
                    "Monthly and yearly Stripe price IDs must be different for plan: " + planCode
            );
        }
    }

    private void requirePrefix(String planCode, String property, String value, String prefix) {
        if (value == null || value.isBlank() || !value.startsWith(prefix)) {
            throw new IllegalStateException(
                    "stripe.plans." + planCode + "." + property + " must start with " + prefix
            );
        }
    }
}
