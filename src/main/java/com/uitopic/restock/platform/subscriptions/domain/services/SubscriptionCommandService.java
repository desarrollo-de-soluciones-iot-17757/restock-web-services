package com.uitopic.restock.platform.subscriptions.domain.services;

import com.uitopic.restock.platform.subscriptions.domain.model.aggregates.Subscription;
import com.uitopic.restock.platform.subscriptions.domain.model.commands.CreateSubscriptionCommand;
import java.util.Optional;

public interface SubscriptionCommandService {
    Optional<Subscription> handle(CreateSubscriptionCommand command);
    String createCheckoutSession(String accountId, String planId);
    void handleStripeWebhook(String payload, String sigHeader);
}
