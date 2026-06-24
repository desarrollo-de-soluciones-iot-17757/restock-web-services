package com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.subscriptions.domain.model.entities.SubscriptionPlan;
import com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.entities.PlanPersistenceEntity;

public final class PlanPersistenceAssembler {

    private PlanPersistenceAssembler() {
        // Private constructor to prevent instantiation
    }

    public static SubscriptionPlan toDomainFromPersistence(PlanPersistenceEntity entity) {
        if (entity == null) return null;
        return new SubscriptionPlan(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getType(),
                entity.getStripeProductId(),
                entity.getLimits(),
                entity.getBenefits(),
                entity.getPrices(),
                entity.isActive()
        );
    }

    public static PlanPersistenceEntity toPersistenceFromDomain(SubscriptionPlan subscriptionsPlan) {
        if (subscriptionsPlan == null) return null;
        var entity = new PlanPersistenceEntity();
        entity.setId(subscriptionsPlan.id());
        entity.setName(subscriptionsPlan.name());
        entity.setDescription(subscriptionsPlan.description());
        entity.setType(subscriptionsPlan.type());
        entity.setStripeProductId(subscriptionsPlan.stripeProductId());
        entity.setLimits(subscriptionsPlan.limits());
        entity.setBenefits(subscriptionsPlan.benefits());
        entity.setPrices(subscriptionsPlan.prices());
        entity.setActive(subscriptionsPlan.active());
        return entity;
    }
}
