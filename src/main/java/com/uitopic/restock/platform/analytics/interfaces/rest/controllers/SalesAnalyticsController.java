package com.uitopic.restock.platform.analytics.interfaces.rest.controllers;

import com.uitopic.restock.platform.analytics.application.internal.outboundservices.acl.ExternalSalesService.RecentSaleItem;
import com.uitopic.restock.platform.analytics.application.internal.queryservices.AnalyticsReportingQueryServiceImpl;
import com.uitopic.restock.platform.analytics.interfaces.rest.resources.RecentSaleResource;
import com.uitopic.restock.platform.analytics.interfaces.rest.transform.RecentSaleResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for sales analytics.
 *
 * Provides endpoints to query recent sales history, optionally filtered by date range,
 * scoped to a given account.
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/sales", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Sales Analytics", description = "Analytics endpoints for sales history and trends.")
public class SalesAnalyticsController {

    private final AnalyticsReportingQueryServiceImpl analyticsReportingQueryService;

    /**
     * Creates a SalesAnalyticsController with the required reporting query service.
     *
     * @param analyticsReportingQueryService service used to execute analytics reporting read operations
     */
    public SalesAnalyticsController(AnalyticsReportingQueryServiceImpl analyticsReportingQueryService) {
        this.analyticsReportingQueryService = analyticsReportingQueryService;
    }

    /**
     * Gets recent sales history for a given account, optionally filtered by date range.
     *
     * Each record includes the branch origin, amount, and supplies sold.
     *
     * @param accountId the account identifier
     * @param startDate optional start date filter (inclusive)
     * @param endDate optional end date filter (inclusive)
     * @return list of recent sale resources
     */
    @Operation(summary = "Get recent sales history",
               description = "Returns a chronological list of recent sales, optionally filtered by date range. Each record includes branch origin, amount, and supplies sold.")
    @GetMapping("/recent-sales")
    public ResponseEntity<List<RecentSaleResource>> getRecentSales(
            @RequestParam String accountId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
    ) {
        log.info("GET /api/v1/sales/recent-sales?accountId='{}'&startDate='{}'&endDate='{}'", accountId, startDate, endDate);
        var recentSales = analyticsReportingQueryService.getRecentSales(accountId, startDate, endDate);
        var resources = recentSales.stream()
                .map(RecentSaleResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }
}
