package com.uitopic.restock.platform.subscriptions.domain.model.entities;

import com.uitopic.restock.platform.subscriptions.domain.model.valueobjects.PlanLimits;
import com.uitopic.restock.platform.subscriptions.domain.model.valueobjects.PlanType;

import java.util.List;
import java.util.Objects;

/**
 * Commercial plan offered by Restock.
 */
public record SubscriptionPlan(
        String id,
        String name,
        String description,
        PlanType type,
        String stripeProductId,
        PlanLimits limits,
        List<String> benefits,
        List<PlanPrice> prices,
        boolean active
) {
    public SubscriptionPlan {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Plan name is required");
        Objects.requireNonNull(type, "Plan type is required");
        Objects.requireNonNull(limits, "Plan limits are required");
        benefits = benefits == null ? List.of() : List.copyOf(benefits);
        prices = prices == null ? List.of() : List.copyOf(prices);
        if (prices.isEmpty()) throw new IllegalArgumentException("At least one plan price is required");
    }

    /**
     * Applies a new catalog definition while preserving provider references
     * already assigned to this persisted plan.
     */
    public SubscriptionPlan updateFromCatalog(SubscriptionPlan catalogDefinition) {
        Objects.requireNonNull(catalogDefinition, "Catalog definition is required");
        if (type != catalogDefinition.type()) {
            throw new IllegalArgumentException("A plan cannot change its type");
        }

        return new SubscriptionPlan(
                id,
                catalogDefinition.name(),
                catalogDefinition.description(),
                catalogDefinition.type(),
                preferConfiguredReference(catalogDefinition.stripeProductId(), stripeProductId),
                catalogDefinition.limits(),
                catalogDefinition.benefits(),
                preserveProviderReferences(catalogDefinition.prices()),
                catalogDefinition.active()
        );
    }

    private List<PlanPrice> preserveProviderReferences(List<PlanPrice> catalogPrices) {
        return catalogPrices.stream()
                .map(catalogPrice -> prices.stream()
                        .filter(currentPrice ->
                                currentPrice.billingInterval() == catalogPrice.billingInterval())
                        .findFirst()
                        .map(currentPrice -> new PlanPrice(
                                catalogPrice.billingInterval(),
                                catalogPrice.money(),
                                preferConfiguredReference(
                                        catalogPrice.stripePriceId(),
                                        currentPrice.stripePriceId()
                                )
                        ))
                        .orElse(catalogPrice))
                .toList();
    }

    public SubscriptionPlan withStripeReferences(
            String productId,
            String monthlyPriceId,
            String yearlyPriceId
    ) {
        return new SubscriptionPlan(
                id,
                name,
                description,
                type,
                productId,
                limits,
                benefits,
                prices.stream()
                        .map(price -> new PlanPrice(
                                price.billingInterval(),
                                price.money(),
                                price.billingInterval() ==
                                        com.uitopic.restock.platform.subscriptions.domain.model.valueobjects.BillingInterval.MONTHLY
                                        ? monthlyPriceId
                                        : yearlyPriceId
                        ))
                        .toList(),
                active
        );
    }

    private static String preferConfiguredReference(String configured, String current) {
        return configured == null || configured.isBlank() ? current : configured;
    }
}
