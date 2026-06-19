package com.uitopic.restock.platform.sales.domain.model.events;

import com.uitopic.restock.platform.sales.domain.model.valueobjects.BatchConsumption;

import java.util.List;

public record SalesOrderCompletedEvent(
        String salesOrderId,
        String accountId,
        String branchId,
        List<BatchConsumption> batchConsumptions
) {
}
