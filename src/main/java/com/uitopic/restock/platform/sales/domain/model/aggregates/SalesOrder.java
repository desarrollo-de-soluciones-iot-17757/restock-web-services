package com.uitopic.restock.platform.sales.domain.model.aggregates;

import com.uitopic.restock.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SalesOrder extends AbstractDomainAggregateRoot<SalesOrder> {
}
