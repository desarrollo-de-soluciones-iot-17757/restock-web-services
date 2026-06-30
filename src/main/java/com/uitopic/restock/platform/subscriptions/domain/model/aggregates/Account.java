package com.uitopic.restock.platform.subscriptions.domain.model.aggregates;

import com.uitopic.restock.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.subscriptions.domain.model.valueobjects.AccountStatus;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account extends AbstractDomainAggregateRoot<Account> {
    private String id;
    private AccountId accountId;
    private String email;
    private String stripeCustomerId;
    @Builder.Default
    private AccountStatus status = AccountStatus.INACTIVE;
    private String currentPlanId;
}
