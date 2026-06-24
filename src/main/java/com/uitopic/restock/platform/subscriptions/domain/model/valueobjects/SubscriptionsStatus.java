package com.uitopic.restock.platform.subscriptions.domain.model.valueobjects;

public enum SubscriptionsStatus {
    PENDING,
    ACTIVE,
    PAST_DUE,
    CANCEL_AT_PERIOD_END,
    CANCELLED;

    public boolean isActive() {
        return this == ACTIVE || this == CANCEL_AT_PERIOD_END;
    }

    public boolean isRenewable() {
        return this == ACTIVE || this == PAST_DUE || this == CANCEL_AT_PERIOD_END;
    }

    public boolean isCancellable() {
        return this == ACTIVE || this == PAST_DUE || this == CANCEL_AT_PERIOD_END;
    }
}
