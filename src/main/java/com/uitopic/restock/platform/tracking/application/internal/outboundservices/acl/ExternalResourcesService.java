package com.uitopic.restock.platform.tracking.application.internal.outboundservices.acl;

import com.uitopic.restock.platform.resources.interfaces.acl.ResourcesContextFacade;
import com.uitopic.restock.platform.resources.interfaces.acl.ResourceStockSnapshot;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.BatchId;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for interacting with external resources to retrieve stock information based on batch IDs. This service uses the ResourcesContextFacade to access the external resources context and obtain the current stock levels for specific batches. If the stock information cannot be retrieved or is invalid (null or zero), an exception is thrown to indicate the failure.
 */
@Service(value = "trackingExternalResourcesService")
@RequiredArgsConstructor
public class ExternalResourcesService {

    // The ResourcesContextFacade is used to interact with the external resources context to retrieve stock information based on batch IDs.
    private final ResourcesContextFacade resourcesContextFacade;

    /**
     * Retrieves the current stock level for a given batch ID from the external resources' context. If the stock value is null or zero, an IllegalStateException is thrown to indicate a failure in retrieving the stock information.
     *
     * @param batchId the unique identifier of the batch for which to retrieve the stock level
     * @return a Pair containing the stock value and the name of the supply associated with the batch ID
     */
    public Pair<Double, String> getCustomSupplyStockAndNameByBatchId(BatchId batchId) {
        var stockAndName = resourcesContextFacade.getSupplyStockAndNameByBatchId(batchId);
        if (stockAndName == null || stockAndName.getLeft() == 0.0) {
            throw new IllegalStateException("Failed to retrieve stock value for batch ID: " + batchId.getBatchId());
        }
        return stockAndName;
    }

    /**
     * Retrieves the stock snapshot needed by tracking to build a complete stock
     * comparison task.
     *
     * @param batchId batch identifier associated with the telemetry reading
     * @return snapshot containing digital stock, custom supply, branch and
     *         account data
     */
    public ResourceStockSnapshot getStockSnapshotByBatchId(BatchId batchId) {
        var snapshot = resourcesContextFacade.getStockSnapshotByBatchId(batchId);
        if (snapshot == null || snapshot.stock() == null || snapshot.stock() == 0.0) {
            throw new IllegalStateException("Failed to retrieve stock value for batch ID: " + batchId.getBatchId());
        }
        return snapshot;
    }

    /**
     * Requests a digital stock adjustment in the resources bounded context.
     *
     * @param batchId batch identifier to adjust
     * @param adjustedQuantity target stock quantity
     */
    public void adjustDigitalStock(BatchId batchId, Double adjustedQuantity) {
        if (adjustedQuantity == null) {
            throw new IllegalArgumentException("Adjusted quantity cannot be null");
        }
        resourcesContextFacade.adjustStockByBatchId(batchId, adjustedQuantity);
    }
}
