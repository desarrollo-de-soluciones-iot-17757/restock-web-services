package com.uitopic.restock.platform.tracking.domain.model.aggregates;

import com.uitopic.restock.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

@Getter
public class Discrepancy extends AbstractDomainAggregateRoot<Discrepancy> {

    // Default constructor for reconstruction from persistence
    public Discrepancy() {

    }
}
