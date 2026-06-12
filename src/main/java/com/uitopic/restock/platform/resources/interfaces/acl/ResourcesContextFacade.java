package com.uitopic.restock.platform.resources.interfaces.acl;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.BatchId;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Inbound ACL facade — exposes resources bounded context inventory operations to other bounded contexts.
 */
public interface ResourcesContextFacade {
    double subtractSupplyStock(String branchId, String supplyId, Integer quantity);
    void addSupplyStockBack(String branchId, String supplyId, Integer quantity, String unit);
    void adjustStock(String branchId, String supplyId, Integer adjustedQuantity, String unit);

    /**
     * Retrieves the current supply stock level for a given batch ID. This method is used to obtain the available stock quantity for a specific batch of supplies, which can be used for inventory management and restocking decisions.
     *
     * @param batchId the unique identifier of the batch for which to retrieve the supply stock level
     * @return the current supply stock level for the specified batch ID, represented as a double value. This value indicates the quantity of stock available for that batch, which can be used to determine if restocking is necessary or to manage inventory levels effectively.
     */
    Pair<Double, String> getSupplyStockAndNameByBatchId(BatchId batchId);
}
