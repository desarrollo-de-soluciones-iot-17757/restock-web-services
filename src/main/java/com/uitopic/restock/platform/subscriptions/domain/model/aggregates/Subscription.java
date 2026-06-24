package com.uitopic.restock.platform.subscriptions.domain.model.aggregates;

import com.uitopic.restock.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.uitopic.restock.platform.subscriptions.domain.model.entities.Payment;
import com.uitopic.restock.platform.subscriptions.domain.model.events.PaymentFailedEvent;
import com.uitopic.restock.platform.subscriptions.domain.model.events.SubscriptionActivatedEvent;
import com.uitopic.restock.platform.subscriptions.domain.model.events.SubscriptionCancelledEvent;
import com.uitopic.restock.platform.subscriptions.domain.model.events.SubscriptionRenewedEvent;
import com.uitopic.restock.platform.subscriptions.domain.model.valueobjects.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class Subscription extends AbstractDomainAggregateRoot<Subscription> {
    private String id;
    private BusinessId businessId;
    private SubscribedPlanSnapshot planSnapshot;
    private SubscriptionsStatus status;
    private BillingCycle billingCycle;
    private boolean cancelAtPeriodEnd;
    private StripeReference stripeReference;
    private List<Payment> payments = new ArrayList<>();
    private List<String> processedStripeEventIds = new ArrayList<>();

    public Subscription() {
    }

    public Subscription(
            String id,
            BusinessId businessId,
            SubscribedPlanSnapshot planSnapshot,
            SubscriptionsStatus status,
            BillingCycle billingCycle,
            boolean cancelAtPeriodEnd,
            StripeReference stripeReference,
            List<Payment> payments,
            List<String> processedStripeEventIds
    ) {
        this.id = id;
        this.businessId = businessId;
        this.planSnapshot = planSnapshot;
        this.status = status;
        this.billingCycle = billingCycle;
        this.cancelAtPeriodEnd = cancelAtPeriodEnd;
        this.stripeReference = stripeReference;
        this.payments = payments == null ? new ArrayList<>() : new ArrayList<>(payments);
        this.processedStripeEventIds = processedStripeEventIds == null
                ? new ArrayList<>()
                : new ArrayList<>(processedStripeEventIds);
    }

    public static Subscription pending(
            String businessId,
            SubscribedPlanSnapshot planSnapshot,
            StripeReference stripeReference
    ) {
        return new Subscription(
                UUID.randomUUID().toString(),
                new BusinessId(businessId),
                Objects.requireNonNull(planSnapshot),
                SubscriptionsStatus.PENDING,
                null,
                false,
                Objects.requireNonNull(stripeReference),
                List.of(),
                List.of()
        );
    }

    public void activate(Payment payment, BillingCycle cycle, Instant occurredAt) {
        requireAcceptedNewPayment(payment);
        if (status != SubscriptionsStatus.PENDING && status != SubscriptionsStatus.PAST_DUE) {
            throw new IllegalStateException("Only pending or past-due subscriptions can be activated");
        }
        applySuccessfulPayment(payment, cycle);
        registerDomainEvent(new SubscriptionActivatedEvent(id, businessId, planSnapshot, cycle, occurredAt));
    }

    public void renew(Payment payment, BillingCycle newCycle, Instant occurredAt) {
        requireAcceptedNewPayment(payment);
        if (!status.isRenewable()) throw new IllegalStateException("Subscription cannot be renewed");
        applySuccessfulPayment(payment, newCycle);
        registerDomainEvent(new SubscriptionRenewedEvent(id, newCycle, occurredAt));
    }

    public void registerPaymentFailure(Payment payment, Instant occurredAt) {
        if (payment.status() != PaymentStatus.FAILED) {
            throw new IllegalArgumentException("A failed payment is required");
        }
        if (hasPayment(payment.stripePaymentIntentId())) return;
        payments.add(payment);
        status = SubscriptionsStatus.PAST_DUE;
        registerDomainEvent(new PaymentFailedEvent(
                id, businessId, payment.id(), payment.stripeInvoiceId(),
                payment.stripePaymentIntentId(), payment.money(), payment.failureCode(),
                payment.failureMessage(), occurredAt
        ));
    }

    public void scheduleCancellation(Instant occurredAt) {
        if (!status.isCancellable()) throw new IllegalStateException("Subscription cannot be cancelled");
        cancelAtPeriodEnd = true;
        status = SubscriptionsStatus.CANCEL_AT_PERIOD_END;
        registerDomainEvent(new SubscriptionCancelledEvent(id, businessId, billingCycle.endsAt(), false, occurredAt));
    }

    public void cancelImmediately(Instant occurredAt) {
        if (!status.isCancellable()) throw new IllegalStateException("Subscription cannot be cancelled");
        cancelAtPeriodEnd = false;
        status = SubscriptionsStatus.CANCELLED;
        registerDomainEvent(new SubscriptionCancelledEvent(id, businessId, occurredAt, true, occurredAt));
    }

    public void completeScheduledCancellation(Instant occurredAt) {
        if (status != SubscriptionsStatus.CANCEL_AT_PERIOD_END) {
            throw new IllegalStateException("Subscription is not scheduled for cancellation");
        }
        cancelAtPeriodEnd = false;
        status = SubscriptionsStatus.CANCELLED;
    }

    public void revokeScheduledCancellation() {
        if (status != SubscriptionsStatus.CANCEL_AT_PERIOD_END) {
            throw new IllegalStateException("Subscription is not scheduled for cancellation");
        }
        cancelAtPeriodEnd = false;
        status = SubscriptionsStatus.ACTIVE;
    }

    public boolean markStripeEventProcessed(String stripeEventId) {
        if (stripeEventId == null || stripeEventId.isBlank()) {
            throw new IllegalArgumentException("Stripe event ID is required");
        }
        if (processedStripeEventIds.contains(stripeEventId)) return false;
        processedStripeEventIds.add(stripeEventId);
        return true;
    }

    public boolean hasPayment(String stripePaymentIntentId) {
        return stripePaymentIntentId != null && payments.stream()
                .anyMatch(payment -> stripePaymentIntentId.equals(payment.stripePaymentIntentId()));
    }

    private void requireAcceptedNewPayment(Payment payment) {
        if (payment == null || !payment.isAccepted()) {
            throw new IllegalStateException("An accepted payment is required");
        }
        if (hasPayment(payment.stripePaymentIntentId())) {
            throw new IllegalStateException("Payment was already processed");
        }
    }

    private void applySuccessfulPayment(Payment payment, BillingCycle cycle) {
        payments.add(payment);
        billingCycle = Objects.requireNonNull(cycle);
        status = SubscriptionsStatus.ACTIVE;
        cancelAtPeriodEnd = false;
        stripeReference = stripeReference.withPaymentIntent(payment.stripePaymentIntentId());
    }

    public List<Payment> getPayments() { return List.copyOf(payments); }
    public List<String> getProcessedStripeEventIds() { return List.copyOf(processedStripeEventIds); }
}
