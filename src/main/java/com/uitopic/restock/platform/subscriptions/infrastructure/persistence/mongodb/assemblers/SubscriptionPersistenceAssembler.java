package com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.subscriptions.domain.model.aggregates.Subscription;
import com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.entities.SubscriptionPersistenceEntity;

public final class SubscriptionPersistenceAssembler {

    private SubscriptionPersistenceAssembler() {

    }

    public static Subscription toDomainFromPersistence(SubscriptionPersistenceEntity entity) {
        return null;
    }

    public static SubscriptionPersistenceEntity toPersistenceFromDomain(Subscription subscription) {
        return null;
    }
}
