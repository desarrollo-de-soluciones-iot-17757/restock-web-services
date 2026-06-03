package com.uitopic.restock.platform.tracking.domain.model.aggregates;

import com.uitopic.restock.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
@Data
public class Discrepancy extends AbstractDomainAggregateRoot<Discrepancy> {

    // Default constructor for reconstruction from persistence
    public Discrepancy() {

    }
}
