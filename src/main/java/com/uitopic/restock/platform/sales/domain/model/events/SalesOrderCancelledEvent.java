package com.uitopic.restock.platform.sales.domain.model.events;

import java.time.LocalDateTime;

public record SalesOrderCancelledEvent(
        String salesOrderId,
        LocalDateTime cancelledAt
) {
}
