package com.uitopic.restock.platform.subscriptions.domain.model.aggregates;

import com.uitopic.restock.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Subscription extends AbstractDomainAggregateRoot<Subscription> {

    // Default constructor for persistence reconstruction
    public Subscription() {

    }
}
