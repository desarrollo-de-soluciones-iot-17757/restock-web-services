package com.uitopic.restock.platform.resources.domain.repositories;

import com.uitopic.restock.platform.resources.domain.model.entities.InventoryTransfer;

import java.util.List;
import java.util.Optional;

public interface InventoryTransferRepository {
    InventoryTransfer save(InventoryTransfer transfer);
    Optional<InventoryTransfer> findById(String id);
    List<InventoryTransfer> findByFromBranchId(String branchId);
    List<InventoryTransfer> findByToBranchId(String branchId);
}
