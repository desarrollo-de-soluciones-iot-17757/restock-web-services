package com.uitopic.restock.platform.subscriptions.domain.model.aggregates;

import com.uitopic.restock.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

@Getter
public class Account extends AbstractDomainAggregateRoot<Account> {

    // Default constructor for persistence reconstruction
    public Account() {

    }
}
