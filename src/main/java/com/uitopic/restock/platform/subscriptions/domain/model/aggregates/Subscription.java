package com.uitopic.restock.platform.subscriptions.domain.model.aggregates;

import com.uitopic.restock.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.subscriptions.domain.model.valueobjects.SubscriptionStatus;
import lombok.*;
import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subscription extends AbstractDomainAggregateRoot<Subscription> {
    private String id;
    private AccountId accountId;
    private String planId;
    private String stripeSubscriptionId;
    private SubscriptionStatus status;
    private Instant currentPeriodStart;
    private Instant currentPeriodEnd;
    private boolean cancelAtPeriodEnd;
}
