package com.uitopic.restock.platform.analytics.application.internal.outboundservices.acl;

import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.entities.BatchPersistenceEntity;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories.BatchPersistenceRepository;
import com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.entities.DiscrepancyPersistenceEntity;
import com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.repositories.DiscrepancyPersistenceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ACL service for accessing external tracking context data.
 * Provides stock discrepancy lookups for the analytics bounded context.
 */
@Slf4j
@Service
public class AnalyticsExternalTrackingService {

    private final DiscrepancyPersistenceRepository discrepancyPersistenceRepository;
    private final BatchPersistenceRepository batchPersistenceRepository;

    public AnalyticsExternalTrackingService(
            DiscrepancyPersistenceRepository discrepancyPersistenceRepository,
            BatchPersistenceRepository batchPersistenceRepository
    ) {
        this.discrepancyPersistenceRepository = discrepancyPersistenceRepository;
        this.batchPersistenceRepository = batchPersistenceRepository;
    }

    public record StockDiscrepancyInfo(
            String discrepancyId,
            Double physicalStock,
            Double systemStock,
            Double difference,
            String riskLevel,
            String status,
            Boolean isConciliated
    ) {}

    /**
     * Retrieves stock discrepancies for a given product.
     *
     * @param productId product identifier
     * @return list of stock discrepancy info
     */
    public List<StockDiscrepancyInfo> getDiscrepanciesByProductId(String productId) {
        log.debug("Fetching stock discrepancies for productId='{}'", productId);
        var allDiscrepancies = discrepancyPersistenceRepository.findAll();

        return allDiscrepancies.stream()
                .map(d -> {
                    var diff = d.getQuantityDifference() != null ? d.getQuantityDifference() : 0.0;
                    return new StockDiscrepancyInfo(
                            d.getId(),
                            0.0,
                            0.0,
                            diff,
                            d.getRiskLevel() != null ? d.getRiskLevel().name() : "LOW",
                            d.getStatus() != null ? d.getStatus().name() : "UNRESOLVED",
                            false
                    );
                })
                .toList();
    }
}
