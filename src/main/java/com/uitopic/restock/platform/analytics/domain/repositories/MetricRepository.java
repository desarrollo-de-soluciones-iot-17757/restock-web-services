package com.uitopic.restock.platform.analytics.domain.repositories;

import com.uitopic.restock.platform.analytics.domain.model.aggregates.Metric;
import com.uitopic.restock.platform.analytics.domain.model.valueobjects.MetricCategory;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Metric persistence operations.
 * Defines the contract for storing and retrieving analytics metrics.
 */
public interface MetricRepository {

    /**
     * Saves a metric.
     *
     * @param metric metric to save
     * @return saved metric
     */
    Metric save(Metric metric);

    /**
     * Finds a metric by its unique identifier.
     *
     * @param id metric identifier
     * @return metric if found
     */
    Optional<Metric> findById(String id);

    /**
     * Finds all metrics for an account filtered by category.
     *
     * @param accountId account identifier
     * @param category  metric category filter
     * @return list of matching metrics
     */
    List<Metric> findAllByAccountIdAndCategory(AccountId accountId, MetricCategory category);

    /**
     * Finds all metrics for an account.
     *
     * @param accountId account identifier
     * @return list of metrics for the account
     */
    List<Metric> findAllByAccountId(AccountId accountId);

    /**
     * Deletes a metric by its unique identifier.
     *
     * @param id metric identifier
     */
    void deleteById(String id);
}
