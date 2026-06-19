package com.uitopic.restock.platform.sales.domain.model.events;

import java.time.LocalDateTime;

public record SalesOrderCreatedEvent(
        String salesOrderId,
        String accountId,
        String branchId,
        LocalDateTime registeredAt
) {
}
