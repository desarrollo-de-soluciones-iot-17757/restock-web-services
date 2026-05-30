package com.uitopic.restock.platform.resources.domain.services;

import com.uitopic.restock.platform.resources.domain.model.entities.InventoryDeduction;
import com.uitopic.restock.platform.resources.domain.model.queries.GetInventoryDeductionsByBatchId;

import java.util.List;
import java.util.Optional;

public interface InventoryDeductionQueryService {
    List<InventoryDeduction> handle(GetInventoryDeductionsByBatchId query);
    Optional<InventoryDeduction> findById(String id);
}
