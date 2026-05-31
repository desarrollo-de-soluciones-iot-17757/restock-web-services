package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.resources.domain.model.entities.Inventory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB repository interface for managing Inventory entities. This repository provides CRUD operations and query methods for handling inventory records in the system, allowing for efficient storage and retrieval of inventory data in a MongoDB database.
 */
@Repository
public interface InventoryMongoRepository extends MongoRepository<Inventory, String> {

    /**
     * Finds all inventory records associated with a specific branch ID. This method is essential for retrieving the inventory details of a particular branch, enabling effective inventory management and decision-making based on the available stock at that branch.
     *
     * @param branchId the unique identifier of the branch for which inventory records are being retrieved
     * @return a list of Inventory records associated with the specified branch ID, or an empty list if no records are found in the database
     */
    List<Inventory> findByBranchId(String branchId);

    /**
     * Finds an inventory record by its batch ID and branch ID. This method allows for retrieving specific inventory information based on the unique identifiers of the batch and branch, facilitating accurate tracking of inventory levels across different locations.
     *
     * @param batchId the unique identifier of the batch associated with the inventory record
     * @param branchId the unique identifier of the branch associated with the inventory record
     * @return an Optional containing the Inventory record if found, or an empty Optional if no matching record exists in the database
     */
    Optional<Inventory> findByBatchIdAndBranchId(String batchId, String branchId);
}
