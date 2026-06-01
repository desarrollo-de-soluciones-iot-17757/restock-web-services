package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.resources.domain.model.entities.InventoryTransfer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MongoDB repository for {@link com.uitopic.restock.platform.resources.domain.model.entities.InventoryTransfer}
 * entities within the resources bounded context.
 */
@Repository
public interface InventoryTransferMongoRepository extends MongoRepository<InventoryTransfer, String> {
    List<InventoryTransfer> findByFromBranchId(String branchId);
    List<InventoryTransfer> findByToBranchId(String branchId);
}
