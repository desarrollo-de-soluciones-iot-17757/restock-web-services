package com.uitopic.restock.platform.subscriptions.domain.model.events;

import com.uitopic.restock.platform.subscriptions.domain.model.valueobjects.BusinessId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.Money;

import java.time.Instant;

public record PaymentFailedEvent(
        String subscriptionId,
        BusinessId businessId,
        String paymentId,
        String stripeInvoiceId,
        String stripePaymentIntentId,
        Money amount,
        String failureCode,
        String failureMessage,
        Instant occurredAt
) {
}
