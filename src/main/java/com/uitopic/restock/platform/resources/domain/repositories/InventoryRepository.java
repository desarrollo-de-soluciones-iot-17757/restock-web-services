package com.uitopic.restock.platform.resources.domain.repositories;

import com.uitopic.restock.platform.resources.domain.model.entities.Inventory;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing inventory operations, including transferring inventory between branches and subtracting inventory from a branch. This interface defines methods to handle inventory-related commands, ensuring that inventory levels are accurately maintained across the system.
 */
public interface InventoryRepository {

    /**
     * Saves the given inventory record to the repository. This method is responsible for persisting the inventory data, allowing for the creation or updating of inventory records in the underlying data store. The method returns an Optional containing the saved Inventory record if the operation is successful, or an empty Optional if the save operation fails.
     *
     * @param inventory the Inventory record to be saved, which may contain information about the batch ID, branch ID, current stock, minimum stock, and inventory state
     * @return an Optional containing the saved Inventory record if the save operation is successful, or an empty Optional if the save operation fails
     */
    Optional<Inventory> save(Inventory inventory);

    /**
     * Finds an inventory record by its unique identifier. This method is essential for retrieving specific inventory details based on the inventory ID, allowing for accurate inventory management and tracking.
     *
     * @param id the unique identifier of the inventory record to be retrieved
     * @return an Optional containing the Inventory record if found, or an empty Optional if no matching record exists
     */
    Optional<Inventory> findById(String id);

    /**
     * Finds all inventory records associated with a specific branch ID. This method is crucial for retrieving the inventory details of a particular branch, allowing for effective inventory management and decision-making based on the available stock at that branch.
     *
     * @param branchId the ID of the branch for which inventory records are being retrieved
     * @return a list of Inventory records associated with the specified branch ID, or an empty list if no records are found
     */
    List<Inventory> findByBranchId(String branchId);

    /**
     * Finds an inventory record based on the provided branch ID and batch ID. This method is essential for retrieving specific inventory details for a given branch and batch, allowing for accurate inventory management and tracking.
     *
     * @param branchId the ID of the branch for which the inventory record is being searched
     * @param batchId the ID of the batch for which the inventory record is being searched
     * @return an Optional containing the Inventory record if found, or an empty Optional if no matching record exists
     */
    Optional<Inventory> findByBranchIdAndBatchId(String branchId, String batchId);
}
