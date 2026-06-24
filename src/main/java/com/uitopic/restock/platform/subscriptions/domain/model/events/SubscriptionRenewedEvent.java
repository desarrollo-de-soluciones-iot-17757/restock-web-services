package com.uitopic.restock.platform.subscriptions.domain.model.events;

import com.uitopic.restock.platform.subscriptions.domain.model.valueobjects.BillingCycle;

import java.time.Instant;

public record SubscriptionRenewedEvent(
        String subscriptionId,
        BillingCycle newBillingCycle,
        Instant occurredAt
) {}
