package com.uitopic.restock.platform.resources.domain.repositories;

import com.uitopic.restock.platform.resources.domain.model.entities.InventoryDeduction;

import java.util.List;
import java.util.Optional;

public interface InventoryDeductionRepository {
    InventoryDeduction save(InventoryDeduction deduction);
    Optional<InventoryDeduction> findById(String id);
    List<InventoryDeduction> findByBranchId(String branchId);
}
