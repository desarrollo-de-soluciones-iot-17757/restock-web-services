package com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.subscriptions.domain.model.aggregates.Subscription;
import com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.entities.SubscriptionPersistenceEntity;

public final class SubscriptionPersistenceAssembler {

    private SubscriptionPersistenceAssembler() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static Subscription toDomainFromPersistence(SubscriptionPersistenceEntity entity) {
        if (entity == null) return null;
        var subscription = new Subscription();
        subscription.setId(entity.getId());
        subscription.setAccountId(entity.getAccountId());
        subscription.setPlanId(entity.getPlanId());
        subscription.setStripeSubscriptionId(entity.getStripeSubscriptionId());
        subscription.setStatus(entity.getStatus());
        subscription.setCurrentPeriodStart(entity.getCurrentPeriodStart());
        subscription.setCurrentPeriodEnd(entity.getCurrentPeriodEnd());
        subscription.setCancelAtPeriodEnd(entity.isCancelAtPeriodEnd());
        return subscription;
    }

    public static SubscriptionPersistenceEntity toPersistenceFromDomain(Subscription subscription) {
        if (subscription == null) return null;
        var entity = new SubscriptionPersistenceEntity();
        if (subscription.getId() != null) {
            entity.setId(subscription.getId());
        }
        entity.setAccountId(subscription.getAccountId());
        entity.setPlanId(subscription.getPlanId());
        entity.setStripeSubscriptionId(subscription.getStripeSubscriptionId());
        entity.setStatus(subscription.getStatus());
        entity.setCurrentPeriodStart(subscription.getCurrentPeriodStart());
        entity.setCurrentPeriodEnd(subscription.getCurrentPeriodEnd());
        entity.setCancelAtPeriodEnd(subscription.isCancelAtPeriodEnd());
        return entity;
    }
}
