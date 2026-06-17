package com.uitopic.restock.platform.analytics.interfaces.rest.controllers;

import com.uitopic.restock.platform.analytics.application.internal.outboundservices.acl.AnalyticsExternalResourcesService.CriticalProductItem;
import com.uitopic.restock.platform.analytics.application.internal.queryservices.AnalyticsReportingQueryServiceImpl;
import com.uitopic.restock.platform.analytics.interfaces.rest.resources.CriticalProductResource;
import com.uitopic.restock.platform.analytics.interfaces.rest.resources.StockDiscrepancyResource;
import com.uitopic.restock.platform.analytics.interfaces.rest.transform.CriticalProductResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for product inventory analytics.
 *
 * Provides endpoints to query critical (low-stock) products and stock discrepancies
 * scoped to a given account or product.
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "System Analytics", description = "Analytics endpoints for system-wide monitoring.")
public class ProductsAnalyticsController {

    private final AnalyticsReportingQueryServiceImpl analyticsReportingQueryService;

    /**
     * Creates a ProductsAnalyticsController with the required reporting query service.
     *
     * @param analyticsReportingQueryService service used to execute analytics reporting read operations
     */
    public ProductsAnalyticsController(AnalyticsReportingQueryServiceImpl analyticsReportingQueryService) {
        this.analyticsReportingQueryService = analyticsReportingQueryService;
    }

    /**
     * Gets products whose stock is below the configured minimum threshold (critical products).
     *
     * Results are ordered by stock deficit descending.
     *
     * @param accountId the account identifier
     * @return list of critical product resources
     */
    @Operation(summary = "Get products with low stock (critical products)",
               description = "Returns a list of products whose stock is below the configured minimum threshold, ordered by stock deficit descending.")
    @GetMapping("/accounts/{accountId}/critical-products")
    public ResponseEntity<List<CriticalProductResource>> getCriticalProducts(
            @PathVariable String accountId
    ) {
        log.info("GET /api/v1/accounts/{}/critical-products", accountId);
        var criticalProducts = analyticsReportingQueryService.getCriticalProducts(accountId);
        var resources = criticalProducts.stream()
                .map(CriticalProductResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    /**
     * Gets stock discrepancies for a specific product.
     *
     * Returns the physical stock, digital stock, and calculated difference.
     * When no discrepancies exist, returns a conciliated status entry.
     *
     * @param productId the product identifier
     * @return list of stock discrepancy resources
     */
    @Operation(summary = "Get stock discrepancies for a specific product",
               description = "Returns the physical stock, digital stock, and calculated difference for a product. Returns conciliated status when no discrepancies exist.")
    @GetMapping("/custom-supplies/{id}/stock-discrepancies")
    public ResponseEntity<List<StockDiscrepancyResource>> getStockDiscrepancies(
            @PathVariable String id
    ) {
        log.info("GET /api/v1/custom-supplies/{}/stock-discrepancies", id);
        var discrepancies = analyticsReportingQueryService.getStockDiscrepancies(id);
        if (discrepancies.isEmpty()) {
            return ResponseEntity.ok(List.of(
                    new StockDiscrepancyResource(null, 0.0, 0.0, 0.0, null, null, true)
            ));
        }
        var resources = discrepancies.stream()
                .map(d -> new StockDiscrepancyResource(
                        d.discrepancyId(),
                        0.0,
                        0.0,
                        d.difference(),
                        d.riskLevel(),
                        d.status(),
                        false
                ))
                .toList();
        return ResponseEntity.ok(resources);
    }
}
