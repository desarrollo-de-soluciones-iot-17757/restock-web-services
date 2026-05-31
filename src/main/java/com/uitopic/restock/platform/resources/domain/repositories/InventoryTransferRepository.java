package com.uitopic.restock.platform.resources.domain.repositories;

import com.uitopic.restock.platform.resources.domain.model.entities.InventoryTransfer;

import java.util.List;
import java.util.Optional;

/**
 * Domain repository port for {@link com.uitopic.restock.platform.resources.domain.model.entities.InventoryTransfer}
 * persistence within the resources bounded context.
 */
public interface InventoryTransferRepository {
    InventoryTransfer save(InventoryTransfer transfer);
    Optional<InventoryTransfer> findById(String id);
    List<InventoryTransfer> findByFromBranchId(String branchId);
    List<InventoryTransfer> findByToBranchId(String branchId);
}
