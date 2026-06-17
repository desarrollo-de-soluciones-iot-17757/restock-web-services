package com.uitopic.restock.platform.analytics.domain.model.commands;

import com.uitopic.restock.platform.analytics.domain.model.valueobjects.DateRange;
import com.uitopic.restock.platform.analytics.domain.model.valueobjects.MetricCategory;
import com.uitopic.restock.platform.analytics.domain.model.valueobjects.MetricType;

/**
 * Command to register a new metric for analytics tracking.
 * Contains the category, type, date range and owning account identifier.
 */
public record RegisterMetricCommand(
        MetricCategory category,
        MetricType type,
        DateRange dateRange,
        String accountId
) {
    /**
     * Compact constructor that validates all required fields.
     */
    public RegisterMetricCommand {
        if (category == null) throw new IllegalArgumentException("category cannot be null");
        if (type == null) throw new IllegalArgumentException("type cannot be null");
        if (dateRange == null) throw new IllegalArgumentException("dateRange cannot be null");
        if (accountId == null || accountId.isBlank()) throw new IllegalArgumentException("accountId cannot be null or blank");
    }
}
