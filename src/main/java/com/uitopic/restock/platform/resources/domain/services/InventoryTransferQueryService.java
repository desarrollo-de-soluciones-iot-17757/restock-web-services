package com.uitopic.restock.platform.resources.domain.services;

import com.uitopic.restock.platform.resources.domain.model.entities.InventoryTransfer;
import com.uitopic.restock.platform.resources.domain.model.queries.GetInventoryTransfersByBatchId;

import java.util.List;
import java.util.Optional;

/**
 * Domain service interface defining the query contract for
 * {@link com.uitopic.restock.platform.resources.domain.model.entities.InventoryTransfer}
 * retrieval within the resources bounded context.
 */
public interface InventoryTransferQueryService {
    List<InventoryTransfer> handle(GetInventoryTransfersByBatchId query);
    Optional<InventoryTransfer> findById(String id);
}
