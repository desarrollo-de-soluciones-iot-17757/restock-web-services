package com.uitopic.restock.platform.resources.infrastructure.repositories;

import com.uitopic.restock.platform.resources.domain.model.entities.InventoryDeduction;
import com.uitopic.restock.platform.resources.domain.repositories.InventoryDeductionRepository;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories.InventoryDeductionMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InventoryDeductionRepositoryImpl implements InventoryDeductionRepository {

    private final InventoryDeductionMongoRepository mongo;

    public InventoryDeductionRepositoryImpl(InventoryDeductionMongoRepository mongo) {
        this.mongo = mongo;
    }

    @Override public InventoryDeduction save(InventoryDeduction deduction) { return mongo.save(deduction); }
    @Override public Optional<InventoryDeduction> findById(String id) { return mongo.findById(id); }
    @Override public List<InventoryDeduction> findByBranchId(String branchId) { return mongo.findByBranchId(branchId); }
}
