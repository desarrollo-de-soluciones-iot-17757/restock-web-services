package com.uitopic.restock.platform.subscriptions.domain.repositories;

import com.uitopic.restock.platform.subscriptions.domain.model.aggregates.Subscription;

import java.util.Optional;

public interface SubscriptionRepository {
    Subscription save(Subscription subscription);
    Optional<Subscription> findByBusinessId(String businessId);
    Optional<Subscription> findByStripeSubscriptionId(String stripeSubscriptionId);
}
