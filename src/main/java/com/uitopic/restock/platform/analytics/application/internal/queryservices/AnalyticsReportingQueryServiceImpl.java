package com.uitopic.restock.platform.analytics.application.internal.queryservices;

import com.uitopic.restock.platform.analytics.application.internal.outboundservices.acl.AnalyticsExternalResourcesService;
import com.uitopic.restock.platform.analytics.application.internal.outboundservices.acl.AnalyticsExternalResourcesService.CriticalProductItem;
import com.uitopic.restock.platform.analytics.application.internal.outboundservices.acl.AnalyticsExternalTrackingService;
import com.uitopic.restock.platform.analytics.application.internal.outboundservices.acl.AnalyticsExternalTrackingService.StockDiscrepancyInfo;
import com.uitopic.restock.platform.analytics.application.internal.outboundservices.acl.ExternalSalesService;
import com.uitopic.restock.platform.analytics.application.internal.outboundservices.acl.ExternalSalesService.RecentSaleItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Application service for analytics reporting read operations.
 * Delegates to external ACL services for critical products, recent sales and stock discrepancies.
 */
@Slf4j
@Service
public class AnalyticsReportingQueryServiceImpl {

    private final AnalyticsExternalResourcesService externalResourcesService;
    private final ExternalSalesService externalSalesService;
    private final AnalyticsExternalTrackingService externalTrackingService;

    public AnalyticsReportingQueryServiceImpl(
            AnalyticsExternalResourcesService externalResourcesService,
            ExternalSalesService externalSalesService,
            AnalyticsExternalTrackingService externalTrackingService
    ) {
        this.externalResourcesService = externalResourcesService;
        this.externalSalesService = externalSalesService;
        this.externalTrackingService = externalTrackingService;
    }

    /**
     * Retrieves critical products for a given account.
     *
     * @param accountId account identifier
     * @return list of critical product items
     */
    public List<CriticalProductItem> getCriticalProducts(String accountId) {
        log.debug("Fetching critical products for account='{}'", accountId);
        return externalResourcesService.getCriticalProductsByAccountId(accountId);
    }

    /**
     * Retrieves recent sales for a given account within a date range.
     *
     * @param accountId account identifier
     * @param startDate start of the date range
     * @param endDate   end of the date range
     * @return list of recent sale items
     */
    public List<RecentSaleItem> getRecentSales(String accountId, LocalDate startDate, LocalDate endDate) {
        log.debug("Fetching recent sales for account='{}' from '{}' to '{}'", accountId, startDate, endDate);
        return externalSalesService.getRecentSales(accountId, startDate, endDate);
    }

    /**
     * Retrieves stock discrepancies for a given product.
     *
     * @param productId product identifier
     * @return list of stock discrepancy info
     */
    public List<StockDiscrepancyInfo> getStockDiscrepancies(String productId) {
        log.debug("Fetching stock discrepancies for productId='{}'", productId);
        return externalTrackingService.getDiscrepanciesByProductId(productId);
    }
}
