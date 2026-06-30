package com.uitopic.restock.platform.subscriptions.domain.model.valueobjects;

/**
 * Enumeration representing the status of a Stripe subscription in the system.
 */
public enum SubscriptionStatus {
    ACTIVE,
    TRIALING,
    PAST_DUE,
    UNPAID,
    CANCELED,
    INCOMPLETE,
    INCOMPLETE_EXPIRED
}
