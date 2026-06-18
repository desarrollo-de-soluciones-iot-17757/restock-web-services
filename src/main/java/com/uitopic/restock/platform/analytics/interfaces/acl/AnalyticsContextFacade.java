package com.uitopic.restock.platform.analytics.interfaces.acl;

import com.uitopic.restock.platform.analytics.domain.model.valueobjects.MetricCategory;

import java.util.List;

/**
 * Inbound ACL facade — exposes analytics bounded context operations to other bounded contexts.
 *
 * Provides methods for querying critical products, metric counts, and stock discrepancies
 * that can be consumed by other bounded contexts without direct dependency on the analytics domain.
 */
public interface AnalyticsContextFacade {

    /**
     * Returns the count of critical products (below minimum stock) for a given account.
     *
     * @param accountId the account identifier
     * @return the count of products whose stock is below the configured minimum threshold
     */
    long getCriticalProductsCount(String accountId);

    /**
     * Returns the total metric count for a given account and category.
     *
     * @param accountId the account identifier
     * @param category the metric category filter
     * @return the count of metrics matching the given account and category
     */
    long getMetricsCountByAccountAndCategory(String accountId, MetricCategory category);

    /**
     * Checks whether a given product has unresolved stock discrepancies.
     *
     * @param productId the product identifier
     * @return true if at least one unresolved discrepancy exists for the product, false otherwise
     */
    boolean hasUnresolvedDiscrepancies(String productId);

    /**
     * Returns the list of product IDs that are currently in critical stock level.
     *
     * @param accountId the account identifier
     * @return list of product identifiers that are below the configured minimum stock threshold
     */
    List<String> getCriticalProductIds(String accountId);
}
