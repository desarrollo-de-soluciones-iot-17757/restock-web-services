package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.resources.domain.model.entities.InventoryTransfer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MongoDB repository interface for managing InventoryTransfer entities. This repository provides CRUD operations and query methods for handling inventory transfer records in the MongoDB database.
 */
@Repository
public interface TransferMongoRepository extends MongoRepository<InventoryTransfer, String> {

    /**
     * Finds a list of InventoryTransfer records where the specified branch ID is the source (fromBranchId) of the transfer. This method is essential for retrieving all inventory transfer records initiated from a specific branch, allowing for effective tracking and management of inventory transfers across different branches in the system.
     *
     * @param fromBranchId the ID of the branch from which the inventory transfer was initiated
     * @return a list of InventoryTransfer records where the specified branch ID is the source of the transfer, or an empty list if no records are found
     */
    List<InventoryTransfer> findByFromBranchId(String fromBranchId);

    /**
     * Finds a list of InventoryTransfer records where the specified branch ID is the destination (toBranchId) of the transfer. This method is essential for retrieving all inventory transfer records received by a specific branch, allowing for effective tracking and management of inventory transfers across different branches in the system.
     *
     * @param toBranchId the ID of the branch to which the inventory transfer was sent
     * @return a list of InventoryTransfer records where the specified branch ID is the destination of the transfer, or an empty list if no records are found
     */
    List<InventoryTransfer> findByToBranchId(String toBranchId);
}
