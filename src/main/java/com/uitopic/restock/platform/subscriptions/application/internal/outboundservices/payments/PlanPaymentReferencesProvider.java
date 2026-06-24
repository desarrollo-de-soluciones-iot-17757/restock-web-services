package com.uitopic.restock.platform.subscriptions.application.internal.outboundservices.payments;

import com.uitopic.restock.platform.subscriptions.domain.model.entities.SubscriptionPlan;

/**
 * Supplies payment-provider references for a plan catalog definition.
 */
public interface PlanPaymentReferencesProvider {
    SubscriptionPlan attachReferences(SubscriptionPlan plan);
}
