package com.uitopic.restock.platform.resources.infrastructure.repositories;

import com.uitopic.restock.platform.resources.domain.model.entities.InventoryTransfer;
import com.uitopic.restock.platform.resources.domain.repositories.InventoryTransferRepository;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories.TransferMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link InventoryTransferRepository} that uses MongoDB for data storage
 * within the resources bounded context.
 */
@Repository
public class InventoryTransferRepositoryImpl implements InventoryTransferRepository {

    private final TransferMongoRepository mongo;

    public InventoryTransferRepositoryImpl(TransferMongoRepository mongo) {
        this.mongo = mongo;
    }

    @Override public InventoryTransfer save(InventoryTransfer transfer) { return mongo.save(transfer); }
    @Override public Optional<InventoryTransfer> findById(String id) { return mongo.findById(id); }
    @Override public List<InventoryTransfer> findByFromBranchId(String branchId) { return mongo.findByFromBranchId(branchId); }
    @Override public List<InventoryTransfer> findByToBranchId(String branchId) { return mongo.findByToBranchId(branchId); }
}
