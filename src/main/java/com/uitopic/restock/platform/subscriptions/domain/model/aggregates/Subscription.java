package com.uitopic.restock.platform.subscriptions.domain.model.aggregates;

import com.uitopic.restock.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Subscription extends AbstractDomainAggregateRoot<Subscription> {

    // Default constructor for persistence reconstruction
    public Subscription() {

    }
}
