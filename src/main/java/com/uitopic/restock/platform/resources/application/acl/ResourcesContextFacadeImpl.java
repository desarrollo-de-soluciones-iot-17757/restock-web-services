package com.uitopic.restock.platform.resources.application.acl;

import com.uitopic.restock.platform.resources.domain.repositories.BatchRepository;
import com.uitopic.restock.platform.resources.domain.repositories.CustomSupplyRepository;
import com.uitopic.restock.platform.resources.domain.services.BatchCommandService;
import com.uitopic.restock.platform.resources.domain.model.aggregates.Batch;
import com.uitopic.restock.platform.resources.domain.model.valueobjects.Stock;
import com.uitopic.restock.platform.resources.interfaces.acl.ResourceStockSnapshot;
import com.uitopic.restock.platform.resources.interfaces.acl.ResourcesContextFacade;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.BatchId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.UnitMeasurement;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /** The CustomSupplyRepository is used to access custom supply data from the database, which may be necessary for certain stock management operations or for retrieving additional information about supplies when needed. It provides methods to query custom supply information and can be used in conjunction with the BatchRepository to provide comprehensive inventory management capabilities within the resources context. */
    private final CustomSupplyRepository customSupplyRepository;

    /**
     * @inheritDocs
     */
    @Override
    public double subtractSupplyStock(String branchId, String supplyId, Integer quantity) {
        return 0;
    }

    /**
     * @inheritDocs
     */
    @Override
    public void addSupplyStockBack(String branchId, String supplyId, Integer quantity, String unit) {

    }

    /**
     * @inheritDocs
     */
    @Override
    @Transactional
    public void adjustStock(String branchId, String supplyId, Double adjustedQuantity, String unit) {
        if (branchId == null || branchId.isBlank()) {
            throw new IllegalArgumentException("Branch ID cannot be null or blank");
        }
        if (supplyId == null || supplyId.isBlank()) {
            throw new IllegalArgumentException("Supply ID cannot be null or blank");
        }
        if (adjustedQuantity == null || adjustedQuantity < 0) {
            throw new IllegalArgumentException("Adjusted quantity cannot be null or negative");
        }

        var batch = batchRepository.findFirstByBranchIdAndCustomSupplyId(branchId, supplyId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Batch not found for branchId='" + branchId + "' and customSupplyId='" + supplyId + "'"
                ));

        var customSupply = customSupplyRepository.findById(supplyId)
                .orElseThrow(() -> new IllegalArgumentException("Custom supply not found: " + supplyId));

        adjustBatchStock(batch, adjustedQuantity, customSupply.getUnitMeasurement());
    }

    /**
     * @inheritDocs
     */
    @Override
    @Transactional
    public void adjustStockByBatchId(BatchId batchId, Double adjustedQuantity) {
        if (batchId == null) {
            throw new IllegalArgumentException("Batch ID cannot be null");
        }
        if (adjustedQuantity == null || adjustedQuantity < 0) {
            throw new IllegalArgumentException("Adjusted quantity cannot be null or negative");
        }

        var batch = batchRepository.findById(batchId.getBatchId())
                .orElseThrow(() -> new IllegalArgumentException("Batch not found: " + batchId.getBatchId()));

        var customSupply = customSupplyRepository.findById(batch.getCustomSupplyId())
                .orElseThrow(() -> new IllegalArgumentException("Custom supply not found: " + batch.getCustomSupplyId()));

        adjustBatchStock(batch, adjustedQuantity, customSupply.getUnitMeasurement());
    }

    /**
     * @inheritDocs
     */
    @Override
    public Pair<Double, String> getSupplyStockAndNameByBatchId(BatchId batchId) {
        var snapshot = getStockSnapshotByBatchId(batchId);
        return Pair.of(snapshot.stock(), snapshot.customSupplyName());
    }

    @Override
    public ResourceStockSnapshot getStockSnapshotByBatchId(BatchId batchId) {
        var batch = batchRepository.findById(batchId.getBatchId());
        if (batch.isEmpty()) {
            return new ResourceStockSnapshot(0.0, "", "", "", "");
        }

        var customSupply = customSupplyRepository.findById(batch.get().getCustomSupplyId());
        if (customSupply.isEmpty()) {
            return new ResourceStockSnapshot(batch.get().getCurrentStock().stock(), batch.get().getCustomSupplyId(), "", batch.get().getBranchId(), batch.get().getAccountId().getAccountId());
        }

        return new ResourceStockSnapshot(
                batch.get().getCurrentStock().stock(),
                batch.get().getCustomSupplyId(),
                customSupply.get().getName(),
                batch.get().getBranchId(),
                batch.get().getAccountId().getAccountId()
        );

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

    private void adjustBatchStock(Batch batch, Double adjustedQuantity, UnitMeasurement unitMeasurement) {
        batch.changeCurrentStock(new Stock(adjustedQuantity, unitMeasurement));
        batchRepository.save(batch);
    }
}
