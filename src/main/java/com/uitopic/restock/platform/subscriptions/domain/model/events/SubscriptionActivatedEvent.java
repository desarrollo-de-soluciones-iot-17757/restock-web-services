package com.uitopic.restock.platform.subscriptions.domain.model.events;

import com.uitopic.restock.platform.subscriptions.domain.model.valueobjects.BillingCycle;
import com.uitopic.restock.platform.subscriptions.domain.model.valueobjects.BusinessId;
import com.uitopic.restock.platform.subscriptions.domain.model.valueobjects.SubscribedPlanSnapshot;

import java.time.Instant;

public record SubscriptionActivatedEvent(
        String subscriptionId,
        BusinessId businessId,
        SubscribedPlanSnapshot plan,
        BillingCycle billingCycle,
        Instant occurredAt
) {}
