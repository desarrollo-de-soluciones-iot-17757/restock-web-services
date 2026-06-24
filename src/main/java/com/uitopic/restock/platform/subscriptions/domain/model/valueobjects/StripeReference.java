package com.uitopic.restock.platform.subscriptions.domain.model.valueobjects;

public record StripeReference(
        String customerId,
        String subscriptionId,
        String latestPaymentIntentId
) {

    public StripeReference withPaymentIntent(String paymentIntentId) {
        return new StripeReference(
                this.customerId,
                this.subscriptionId,
                paymentIntentId
        );
    }
}
