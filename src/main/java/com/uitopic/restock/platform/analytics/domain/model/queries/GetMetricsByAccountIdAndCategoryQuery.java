package com.uitopic.restock.platform.analytics.domain.model.queries;

import com.uitopic.restock.platform.analytics.domain.model.valueobjects.MetricCategory;

/**
 * Query to retrieve metrics filtered by account identifier and category.
 */
public record GetMetricsByAccountIdAndCategoryQuery(
        String accountId,
        MetricCategory category
) {
    /**
     * Compact constructor that validates all required fields.
     */
    public GetMetricsByAccountIdAndCategoryQuery {
        if (accountId == null || accountId.isBlank())
            throw new IllegalArgumentException("accountId cannot be null or blank");
        if (category == null)
            throw new IllegalArgumentException("category cannot be null");
    }
}
