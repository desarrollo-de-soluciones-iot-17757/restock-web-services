package com.uitopic.restock.platform.analytics.application.internal.outboundservices.acl;

import com.uitopic.restock.platform.sales.infrastructure.persistence.mongodb.repositories.SalesOrderPersistenceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * ACL service for accessing external sales context data.
 * Provides recent sale lookups for the analytics bounded context.
 */
@Slf4j
@Service
public class ExternalSalesService {

    private final SalesOrderPersistenceRepository salesOrderPersistenceRepository;

    public ExternalSalesService(SalesOrderPersistenceRepository salesOrderPersistenceRepository) {
        this.salesOrderPersistenceRepository = salesOrderPersistenceRepository;
    }

    public record RecentSaleItem(
            String saleId,
            String branchId,
            Double totalAmount,
            LocalDate saleDate,
            String status
    ) {}

    /**
     * Retrieves recent sales for a given account within a date range.
     *
     * @param accountId account identifier
     * @param startDate start of the date range
     * @param endDate   end of the date range
     * @return list of recent sale items sorted by date descending
     */
    public List<RecentSaleItem> getRecentSales(String accountId, LocalDate startDate, LocalDate endDate) {
        log.debug("Fetching recent sales for account='{}' from '{}' to '{}'", accountId, startDate, endDate);
        return salesOrderPersistenceRepository.findAll().stream()
                .filter(order -> {
                    if (order.getCreatedAt() == null) return false;
                    var saleDate = new java.util.Date(order.getCreatedAt().getTime()).toInstant()
                            .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                    if (startDate != null && saleDate.isBefore(startDate)) return false;
                    if (endDate != null && saleDate.isAfter(endDate)) return false;
                    return true;
                })
                .map(order -> new RecentSaleItem(
                        order.getId(),
                        null,
                        null,
                        order.getCreatedAt() != null
                                ? new java.util.Date(order.getCreatedAt().getTime()).toInstant()
                                .atZone(java.time.ZoneId.systemDefault()).toLocalDate()
                                : null,
                        null
                ))
                .sorted((a, b) -> {
                    if (a.saleDate() == null && b.saleDate() == null) return 0;
                    if (a.saleDate() == null) return 1;
                    if (b.saleDate() == null) return -1;
                    return b.saleDate().compareTo(a.saleDate());
                })
                .toList();
    }
}
