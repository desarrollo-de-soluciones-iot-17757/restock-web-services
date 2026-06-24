package com.uitopic.restock.platform.subscriptions.domain.model.entities;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.Money;
import com.uitopic.restock.platform.subscriptions.domain.model.valueobjects.PaymentStatus;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record Payment(
        String id,
        Money money,
        PaymentStatus status,
        String stripePaymentIntentId,
        String stripeInvoiceId,
        String failureCode,
        String failureMessage,
        Instant processedAt
) {
    public Payment {
        Objects.requireNonNull(money, "Payment amount is required");
        Objects.requireNonNull(status, "Payment status is required");
    }

    public static Payment accepted(String paymentIntentId, String invoiceId, Money money, Instant processedAt) {
        return new Payment(UUID.randomUUID().toString(), money, PaymentStatus.ACCEPTED,
                paymentIntentId, invoiceId, null, null, processedAt);
    }

    public static Payment failed(
            String paymentIntentId,
            String invoiceId,
            Money money,
            String failureCode,
            String failureMessage,
            Instant processedAt
    ) {
        return new Payment(UUID.randomUUID().toString(), money, PaymentStatus.FAILED,
                paymentIntentId, invoiceId, failureCode, failureMessage, processedAt);
    }

    public boolean isAccepted() {
        return status == PaymentStatus.ACCEPTED;
    }
}
