package com.uitopic.restock.platform.subscriptions.domain.model.events;

import com.uitopic.restock.platform.subscriptions.domain.model.valueobjects.BusinessId;

import java.time.Instant;

public record SubscriptionCancelledEvent(
        String subscriptionId,
        BusinessId businessId,
        Instant accessUntil,
        boolean immediate,
        Instant occurredAt
) {
}
