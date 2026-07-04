package com.uitopic.restock.platform.subscriptions.domain.model.commands;

import java.time.Instant;

public record CreateSubscriptionCommand(
        String accountId,
        String planId,
        String stripeSubscriptionId,
        String stripeCustomerId,
        String status,
        Instant currentPeriodStart,
        Instant currentPeriodEnd,
        boolean cancelAtPeriodEnd
) {
}
