package com.uitopic.restock.platform.subscriptions.domain.model.valueobjects;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public record BillingCycle(
        Instant startsAt,
        Instant endsAt
) {
    public BillingCycle {
        if (startsAt == null || endsAt == null || !endsAt.isAfter(startsAt)) {
            throw new IllegalArgumentException("Billing cycle end must be after its start");
        }
    }

    public int daysRemaining(Clock clock) {
        long days = ChronoUnit.DAYS.between(Instant.now(clock), endsAt);
        return (int) Math.max(days, 0);
    }

    public boolean isExpiringSoon(Clock clock) {
        return daysRemaining(clock) <= 7 && !isExpired(clock);
    }

    public boolean isExpired(Clock clock) {
        return !Instant.now(clock).isBefore(endsAt);
    }
}
