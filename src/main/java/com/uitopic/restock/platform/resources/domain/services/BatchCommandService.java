package com.uitopic.restock.platform.resources.domain.services;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Batch;
import com.uitopic.restock.platform.resources.domain.model.commands.CreateBatchCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.SubtractInventoryCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.TransferInventoryCommand;
import com.uitopic.restock.platform.resources.domain.model.entities.InventoryDeduction;
import com.uitopic.restock.platform.resources.domain.model.entities.InventoryTransfer;

public interface BatchCommandService {
    Batch handle(CreateBatchCommand command);
    InventoryTransfer handle(TransferInventoryCommand command);
    InventoryDeduction handle(SubtractInventoryCommand command);
    double subtractStock(String branchId, String customSupplyId, double quantity);
    void addStockBack(String branchId, String customSupplyId, double quantity, String unit);
    void adjustStock(String branchId, String customSupplyId, double adjustedQuantity, String unit);
}
