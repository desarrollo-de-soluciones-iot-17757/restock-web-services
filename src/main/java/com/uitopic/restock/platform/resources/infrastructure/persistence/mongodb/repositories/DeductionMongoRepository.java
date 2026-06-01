package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.resources.domain.model.entities.InventoryDeduction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MongoDB repository interface for managing InventoryDeduction entities. This repository provides CRUD operations and query methods for handling inventory deductions in the system, allowing for efficient storage and retrieval of inventory deduction records in a MongoDB database.
 */
@Repository
public interface DeductionMongoRepository extends MongoRepository<InventoryDeduction, String> {

    /**
     * Finds a list of InventoryDeduction records associated with a specific branch ID. This method is essential for retrieving all inventory deduction records for a given branch, allowing for effective tracking and management of inventory deductions across different branches in the system.
     *
     * @param branchId the ID of the branch for which inventory deduction records are being retrieved
     * @return a list of InventoryDeduction records associated with the specified branch ID, or an empty list if no records are found
     */
    List<InventoryDeduction> findByBranchId(String branchId);
}
