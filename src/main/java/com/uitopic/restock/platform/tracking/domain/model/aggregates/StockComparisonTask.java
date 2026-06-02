package com.uitopic.restock.platform.tracking.domain.model.aggregates;

import com.uitopic.restock.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

@Getter
public class StockComparisonTask extends AbstractDomainAggregateRoot<StockComparisonTask> {

    // Default constructor for reconstruction from persistence
    public StockComparisonTask() {

    }
}
