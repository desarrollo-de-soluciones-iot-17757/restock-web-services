package com.uitopic.restock.platform.analytics.application.acl;

import com.uitopic.restock.platform.analytics.application.internal.outboundservices.acl.AnalyticsExternalResourcesService;
import com.uitopic.restock.platform.analytics.application.internal.outboundservices.acl.AnalyticsExternalTrackingService;
import com.uitopic.restock.platform.analytics.application.internal.queryservices.AnalyticsReportingQueryServiceImpl;
import com.uitopic.restock.platform.analytics.domain.model.queries.GetMetricsByAccountIdAndCategoryQuery;
import com.uitopic.restock.platform.analytics.domain.model.valueobjects.MetricCategory;
import com.uitopic.restock.platform.analytics.domain.services.MetricQueryService;
import com.uitopic.restock.platform.analytics.interfaces.acl.AnalyticsContextFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the AnalyticsContextFacade — exposes analytics functionality to other bounded contexts.
 *
 * Delegates read operations to the internal analytics reporting query service and the metric query service,
 * and delegates cross-context queries to the external tracking ACL service.
 */
@Slf4j
@Service
public class AnalyticsContextFacadeImpl implements AnalyticsContextFacade {

    private final AnalyticsReportingQueryServiceImpl analyticsReportingQueryService;
    private final MetricQueryService metricQueryService;
    private final AnalyticsExternalTrackingService externalTrackingService;

    /**
     * Creates an AnalyticsContextFacadeImpl with the required services.
     *
     * @param analyticsReportingQueryService service used for analytics reporting queries
     * @param metricQueryService service used for metric queries
     * @param externalTrackingService service used to query tracking data from external contexts
     */
    public AnalyticsContextFacadeImpl(
            AnalyticsReportingQueryServiceImpl analyticsReportingQueryService,
            MetricQueryService metricQueryService,
            AnalyticsExternalTrackingService externalTrackingService
    ) {
        this.analyticsReportingQueryService = analyticsReportingQueryService;
        this.metricQueryService = metricQueryService;
        this.externalTrackingService = externalTrackingService;
    }

    /**
     * Returns the count of critical products (below minimum stock) for a given account.
     *
     * @param accountId the account identifier
     * @return the count of critical products
     */
    @Override
    public long getCriticalProductsCount(String accountId) {
        log.debug("Getting critical products count for account '{}'", accountId);
        return analyticsReportingQueryService.getCriticalProducts(accountId).size();
    }

    /**
     * Returns the total metric count for a given account and category.
     *
     * @param accountId the account identifier
     * @param category the metric category filter
     * @return the count of metrics matching the criteria
     */
    @Override
    public long getMetricsCountByAccountAndCategory(String accountId, MetricCategory category) {
        log.debug("Getting metrics count for account '{}' and category '{}'", accountId, category);
        var query = new GetMetricsByAccountIdAndCategoryQuery(accountId, category);
        return metricQueryService.handle(query).size();
    }

    /**
     * Checks whether a given product has unresolved stock discrepancies.
     *
     * @param productId the product identifier
     * @return true if there is at least one unresolved discrepancy, false otherwise
     */
    @Override
    public boolean hasUnresolvedDiscrepancies(String productId) {
        log.debug("Checking unresolved discrepancies for product '{}'", productId);
        return externalTrackingService.getDiscrepanciesByProductId(productId).stream()
                .anyMatch(d -> "UNRESOLVED".equals(d.status()));
    }

    /**
     * Returns the list of product IDs that are currently in critical stock level.
     *
     * @param accountId the account identifier
     * @return list of product IDs with critical stock levels
     */
    @Override
    public List<String> getCriticalProductIds(String accountId) {
        log.debug("Getting critical product IDs for account '{}'", accountId);
        return analyticsReportingQueryService.getCriticalProducts(accountId).stream()
                .map(AnalyticsExternalResourcesService.CriticalProductItem::productId)
                .toList();
    }
}
