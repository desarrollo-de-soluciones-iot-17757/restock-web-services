package com.uitopic.restock.platform.analytics.domain.services;

import com.uitopic.restock.platform.analytics.domain.model.aggregates.Metric;
import com.uitopic.restock.platform.analytics.domain.model.queries.GetMetricsByAccountIdAndCategoryQuery;

import java.util.List;
import java.util.Optional;

/**
 * Domain service interface for metric read operations.
 */
public interface MetricQueryService {

    /**
     * Handles a query to retrieve metrics filtered by account and category.
     *
     * @param query query with account and category filters
     * @return list of matching metrics
     */
    List<Metric> handle(GetMetricsByAccountIdAndCategoryQuery query);

    /**
     * Finds a metric by its unique identifier.
     *
     * @param id metric identifier
     * @return metric if found
     */
    Optional<Metric> findById(String id);
}
