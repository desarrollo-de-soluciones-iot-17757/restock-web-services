package com.uitopic.restock.platform.resources.application.acl;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Batch;
import com.uitopic.restock.platform.resources.domain.model.commands.AddBackBatchStockCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.SubtractBatchStockCommand;
import com.uitopic.restock.platform.resources.domain.repositories.BatchRepository;
import com.uitopic.restock.platform.resources.domain.repositories.CustomSupplyRepository;
import com.uitopic.restock.platform.resources.domain.services.BatchCommandService;
import com.uitopic.restock.platform.resources.domain.services.BatchQueryService;
import com.uitopic.restock.platform.resources.interfaces.acl.ResourceStockSnapshot;
import com.uitopic.restock.platform.resources.interfaces.acl.ResourcesContextFacade;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.BatchId;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * Implementation of the ResourcesContextFacade interface, providing methods to manage supply stock levels and retrieve stock information based on batch IDs. This class serves as an adapter between the resources bounded context and other bounded contexts that need to interact with inventory data.
 */
@Service
@RequiredArgsConstructor
public class ResourcesContextFacadeImpl implements ResourcesContextFacade {

    // The BatchCommandService is used to perform operations related to supply stock management, such as subtracting stock, adding stock back, and adjusting stock levels. It encapsulates the business logic for managing inventory and ensures that all stock-related operations are handled consistently across the application.
    private final BatchCommandService batchCommandService;

    // The BatchRepository is used to access batch data from the database, allowing the implementation to retrieve current stock levels for specific batches when requested. It provides methods to query batch information and is essential for implementing the getSupplyStockByBatchId method, which requires access to batch data to return accurate stock levels.
    private final BatchRepository batchRepository;

    private final BatchQueryService batchQueryService;

    /** The CustomSupplyRepository is used to access custom supply data from the database, which may be necessary for certain stock management operations or for retrieving additional information about supplies when needed. It provides methods to query custom supply information and can be used in conjunction with the BatchRepository to provide comprehensive inventory management capabilities within the resources context. */
    private final CustomSupplyRepository customSupplyRepository;

    /**
     * @inheritDocs
     */
    @Override
    public double subtractSupplyStock(String branchId, String batchId, Integer quantity) {
        return batchCommandService.handle(new SubtractBatchStockCommand(batchId, quantity.doubleValue()));
    }

    /**
     * @inheritDocs
     */
    @Override
    public void addSupplyStockBack(String branchId, String batchId, Integer quantity) {
        batchCommandService.handle(new AddBackBatchStockCommand(batchId, quantity.doubleValue()));
    }

    /**
     * @inheritDocs
     */
    @Override
    public Pair<Double, String> getSupplyStockAndNameByBatchId(BatchId batchId) {
        var batch = batchRepository.findById(batchId.getBatchId());
        if (batch.isEmpty()) {
            return Pair.of(0.0, "");
        }

        var customSupply = customSupplyRepository.findById(batch.get().getCustomSupplyId());
        if (customSupply.isEmpty()) {
            return Pair.of(0.0, "");
        }

        return Pair.of(
                batch.get().getCurrentStock().stock(),
                customSupply.get().getName()
        );

    }

    @Override
    public ResourceStockSnapshot getStockSnapshotByBatchId(BatchId batchId) {
        return null;
    }

    /**
     * @inheritDocs
     */
    @Override
    public double getTotalStockByCustomSupplyIdAndBranchId(String customSupplyId, String branchId) {
        return batchRepository.findByCustomSupplyId(customSupplyId).stream()
                .filter(batch -> batch.getBranchId().equals(branchId))
                .mapToDouble(batch -> batch.getCurrentStock().stock())
                .sum();
    }

    @Override
    public String resolveAvailableBatchId(String branchId, String customSupplyId, Double quantityNeeded) {
        List<Batch> filteredBatches = batchQueryService.findAllByFilters(null, branchId, customSupplyId);

        return filteredBatches.stream()
                .filter(batch -> batch.getCurrentStock().stock() > 0)
                .sorted(Comparator.comparing(Batch::getEntryDate))
                .findFirst()
                .map(Batch::getId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No available stock or active batches found for supply: " + customSupplyId + " in branch: " + branchId
                ));
    }

    @Override
    public void adjustStock(String branchId, String supplyId, Double adjustedQuantity, String unit) {

    }

    @Override
    public void adjustStockByBatchId(BatchId batchId, Double adjustedQuantity) {

    }

}