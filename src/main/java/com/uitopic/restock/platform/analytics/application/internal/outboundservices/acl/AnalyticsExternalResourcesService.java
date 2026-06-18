package com.uitopic.restock.platform.analytics.application.internal.outboundservices.acl;

import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.entities.BatchPersistenceEntity;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.entities.BranchPersistenceEntity;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.entities.CustomSupplyPersistenceEntity;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories.BatchPersistenceRepository;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories.BranchPersistenceRepository;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories.CustomSupplyPersistenceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * ACL service for accessing external resources context data.
 * Provides branch, critical product and custom supply lookups for the analytics bounded context.
 */
@Slf4j
@Service
public class AnalyticsExternalResourcesService {

    private final CustomSupplyPersistenceRepository customSupplyPersistenceRepository;
    private final BranchPersistenceRepository branchPersistenceRepository;
    private final BatchPersistenceRepository batchPersistenceRepository;

    public AnalyticsExternalResourcesService(
            CustomSupplyPersistenceRepository customSupplyPersistenceRepository,
            BranchPersistenceRepository branchPersistenceRepository,
            BatchPersistenceRepository batchPersistenceRepository
    ) {
        this.customSupplyPersistenceRepository = customSupplyPersistenceRepository;
        this.branchPersistenceRepository = branchPersistenceRepository;
        this.batchPersistenceRepository = batchPersistenceRepository;
    }

    public record CriticalProductItem(
            String productId,
            String productName,
            String supplyId,
            Double totalStock,
            Double minStock,
            Double maxStock,
            Double stockDeficit,
            String branchName,
            String branchId,
            String unitMeasurement
    ) {}

    public record BranchInfo(String id, String name) {}

    /**
     * Retrieves branch information for a given account.
     *
     * @param accountId account identifier
     * @return list of branch info
     */
    public List<BranchInfo> getBranchesByAccountId(String accountId) {
        log.debug("Fetching branches for account='{}'", accountId);
        com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId accId =
                new com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId(accountId);
        return branchPersistenceRepository.findByAccountId(accId).stream()
                .map(b -> new BranchInfo(b.getId(), b.getName()))
                .toList();
    }

    /**
     * Retrieves critical products (supplies with total stock below minimum) for a given account.
     *
     * @param accountId account identifier
     * @return list of critical product items sorted by deficit descending
     */
    public List<CriticalProductItem> getCriticalProductsByAccountId(String accountId) {
        log.debug("Fetching critical products for account='{}'", accountId);
        com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId accId =
                new com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId(accountId);

        var supplies = customSupplyPersistenceRepository.findByAccountId(accId);
        var branches = branchPersistenceRepository.findByAccountId(accId);

        // Fetch all batches per branch up front
        var branchBatches = branches.stream()
                .collect(java.util.stream.Collectors.toMap(
                        BranchPersistenceEntity::getId,
                        branch -> batchPersistenceRepository.findByBranchId(branch.getId())
                ));

        return supplies.stream()
                .flatMap(supply -> branches.stream()
                        .map(branch -> {
                            var batches = branchBatches.getOrDefault(branch.getId(), List.of()).stream()
                                    .filter(b -> b.getCustomSupplyId().equals(supply.getId()))
                                    .toList();
                            var totalStock = batches.stream()
                                    .mapToDouble(b -> b.getCurrentStock() != null ? b.getCurrentStock().getStock() : 0.0)
                                    .sum();
                            var minStock = supply.getStockRange() != null ? supply.getStockRange().getMinStock() : 0.0;
                            var maxStock = supply.getStockRange() != null ? supply.getStockRange().getMaxStock() : 0.0;
                            var deficit = Math.max(0.0, minStock - totalStock);
                            var supplyName = supply.getSupply() != null ? supply.getSupply().getName() : supply.getName();
                            var unit = supply.getUnitMeasurement() != null ? supply.getUnitMeasurement().unitName() : "";

                            return new CriticalProductItem(
                                    supply.getId(),
                                    supplyName,
                                    supply.getSupplyId(),
                                    totalStock,
                                    minStock,
                                    maxStock,
                                    deficit,
                                    branch.getName(),
                                    branch.getId(),
                                    unit
                            );
                        })
                )
                .filter(item -> item.totalStock() < item.minStock())
                .sorted(Comparator.comparingDouble(CriticalProductItem::stockDeficit).reversed())
                .toList();
    }

    /**
     * Retrieves custom supplies for a given account.
     *
     * @param accountId account identifier
     * @return list of custom supply persistence entities
     */
    public List<?> getCustomSuppliesByAccountId(String accountId) {
        log.debug("Fetching custom supplies for account='{}'", accountId);
        com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId accId =
                new com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId(accountId);
        return customSupplyPersistenceRepository.findByAccountId(accId);
    }
}
