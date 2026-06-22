package com.uitopic.restock.platform.analytics.application.internal.queryservices;

import com.uitopic.restock.platform.analytics.application.internal.outboundservices.acl.AnalyticsExternalResourcesService;
import com.uitopic.restock.platform.analytics.application.internal.outboundservices.acl.AnalyticsExternalResourcesService.CriticalProductItem;
import com.uitopic.restock.platform.analytics.application.internal.outboundservices.acl.AnalyticsExternalTrackingService;
import com.uitopic.restock.platform.analytics.application.internal.outboundservices.acl.AnalyticsExternalTrackingService.StockDiscrepancyInfo;
import com.uitopic.restock.platform.analytics.application.internal.outboundservices.acl.ExternalSalesService;
import com.uitopic.restock.platform.analytics.application.internal.outboundservices.acl.ExternalSalesService.RecentSaleItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class AnalyticsReportingQueryServiceImplTest {

    @Mock
    private AnalyticsExternalResourcesService externalResourcesService;

    @Mock
    private ExternalSalesService externalSalesService;

    @Mock
    private AnalyticsExternalTrackingService externalTrackingService;

    @InjectMocks
    private AnalyticsReportingQueryServiceImpl analyticsReportingQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCriticalProducts() {
        // Arrange
        String accountId = "account-123";
        var criticalProduct = new CriticalProductItem(
                "prod-1",
                "Product A",
                "supply-1",
                5.0,
                10.0,
                20.0,
                5.0,
                "Main Branch",
                "branch-1",
                "kg"
        );
        when(externalResourcesService.getCriticalProductsByAccountId(accountId))
                .thenReturn(List.of(criticalProduct));

        // Act
        var result = analyticsReportingQueryService.getCriticalProducts(accountId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Product A", result.get(0).productName());
        verify(externalResourcesService, times(1)).getCriticalProductsByAccountId(accountId);
    }

    @Test
    void testGetRecentSales() {
        // Arrange
        String accountId = "account-123";
        LocalDate start = LocalDate.now().minusDays(7);
        LocalDate end = LocalDate.now();
        var saleItem = new RecentSaleItem("sale-1", "branch-1", 100.0, LocalDate.now(), "COMPLETED");
        when(externalSalesService.getRecentSales(accountId, start, end))
                .thenReturn(List.of(saleItem));

        // Act
        var result = analyticsReportingQueryService.getRecentSales(accountId, start, end);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("sale-1", result.get(0).saleId());
        verify(externalSalesService, times(1)).getRecentSales(accountId, start, end);
    }

    @Test
    void testGetStockDiscrepancies() {
        // Arrange
        String productId = "prod-1";
        var discrepancy = new StockDiscrepancyInfo("disc-1", 7.0, 10.0, -3.0, "HIGH", "PENDING", false);
        when(externalTrackingService.getDiscrepanciesByProductId(productId))
                .thenReturn(List.of(discrepancy));

        // Act
        var result = analyticsReportingQueryService.getStockDiscrepancies(productId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("disc-1", result.get(0).discrepancyId());
        verify(externalTrackingService, times(1)).getDiscrepanciesByProductId(productId);
    }
}
