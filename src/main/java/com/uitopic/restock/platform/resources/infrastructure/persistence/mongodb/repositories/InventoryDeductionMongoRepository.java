package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.resources.domain.model.entities.InventoryDeduction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MongoDB repository for {@link com.uitopic.restock.platform.resources.domain.model.entities.InventoryDeduction}
 * entities within the resources bounded context.
 */
@Repository
public interface InventoryDeductionMongoRepository extends MongoRepository<InventoryDeduction, String> {
    List<InventoryDeduction> findByBranchId(String branchId);
}
