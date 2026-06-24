package com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.subscriptions.domain.model.aggregates.Subscription;
import com.uitopic.restock.platform.subscriptions.domain.model.valueobjects.BusinessId;
import com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.entities.SubscriptionPersistenceEntity;

public final class SubscriptionPersistenceAssembler {

    private SubscriptionPersistenceAssembler() {

    }

    public static Subscription toDomainFromPersistence(SubscriptionPersistenceEntity entity) {
        if (entity == null) return null;
        return new Subscription(
                entity.getId(),
                new BusinessId(entity.getBusinessId()),
                entity.getPlanSnapshot(),
                entity.getStatus(),
                entity.getBillingCycle(),
                entity.isCancelAtPeriodEnd(),
                entity.getStripeReference(),
                entity.getPayments(),
                entity.getProcessedStripeEventIds()
        );
    }

    public static SubscriptionPersistenceEntity toPersistenceFromDomain(Subscription subscription) {
        if (subscription == null) return null;
        var entity = new SubscriptionPersistenceEntity();
        entity.setId(subscription.getId());
        entity.setBusinessId(subscription.getBusinessId().businessId());
        entity.setPlanSnapshot(subscription.getPlanSnapshot());
        entity.setStatus(subscription.getStatus());
        entity.setBillingCycle(subscription.getBillingCycle());
        entity.setCancelAtPeriodEnd(subscription.isCancelAtPeriodEnd());
        entity.setStripeReference(subscription.getStripeReference());
        entity.setPayments(subscription.getPayments());
        entity.setProcessedStripeEventIds(subscription.getProcessedStripeEventIds());
        return entity;
    }
}
