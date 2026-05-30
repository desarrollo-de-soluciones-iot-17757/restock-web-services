package com.uitopic.restock.platform.resources.domain.services;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Batch;
import com.uitopic.restock.platform.resources.domain.model.commands.CreateBatchCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.SubtractInventoryCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.TransferInventoryCommand;
import com.uitopic.restock.platform.resources.domain.model.entities.InventoryDeduction;
import com.uitopic.restock.platform.resources.domain.model.entities.InventoryTransfer;

import java.util.Optional;

public interface BatchCommandService {
    Optional<Batch> handle(CreateBatchCommand command);
    Optional<InventoryTransfer> handle(TransferInventoryCommand command);
    Optional<InventoryDeduction> handle(SubtractInventoryCommand command);
    double subtractStock(String branchId, String customSupplyId, Integer quantity);
    void addStockBack(String branchId, String customSupplyId, Integer quantity, String unit);
    void adjustStock(String branchId, String customSupplyId, Integer adjustedQuantity, String unit);
}
