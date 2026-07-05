package com.uitopic.restock.platform.subscriptions.domain.repositories;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.subscriptions.domain.model.aggregates.Subscription;
import java.util.Optional;

public interface SubscriptionRepository {
    Optional<Subscription> findById(String id);
    Optional<Subscription> findByAccountId(AccountId accountId);
    Optional<Subscription> findByStripeSubscriptionId(String stripeSubscriptionId);
    Subscription save(Subscription subscription);
    void deleteById(String id);
}
