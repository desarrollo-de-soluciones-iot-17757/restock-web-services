package com.uitopic.restock.platform.resources.infrastructure.repositories;

import com.uitopic.restock.platform.resources.domain.model.entities.InventoryDeduction;
import com.uitopic.restock.platform.resources.domain.repositories.InventoryDeductionRepository;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories.DeductionMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the InventoryDeductionRepository interface, providing methods to manage inventory deductions in the system. This class interacts with the InventoryDeductionMongoRepository to perform CRUD operations and query methods for handling inventory deductions in a MongoDB database, ensuring efficient storage and retrieval of inventory deduction records.
 */
@Repository
public class InventoryDeductionRepositoryImpl implements InventoryDeductionRepository {

    // The InventoryDeductionMongoRepository is injected into this class to facilitate interactions with the MongoDB database for managing inventory deduction records.
    private final DeductionMongoRepository mongo;

    // Constructor for InventoryDeductionRepositoryImpl, which takes an InventoryDeductionMongoRepository as a parameter and assigns it to the mongo field. This allows the class to use the MongoDB repository for performing operations related to inventory deductions.
    public InventoryDeductionRepositoryImpl(DeductionMongoRepository mongo) {
        this.mongo = mongo;
    }

    /**
     * Saves the given InventoryDeduction record to the repository. This method is responsible for persisting the inventory deduction data, allowing for the creation or updating of inventory deduction records in the underlying MongoDB database. The method returns the saved InventoryDeduction record if the operation is successful.
     *
     * @param deduction the InventoryDeduction record to be saved, which may contain information about the branch ID, batch ID, quantity deducted, and deduction date
     * @return the saved InventoryDeduction record if the save operation is successful
     */
    @Override
    public InventoryDeduction save(InventoryDeduction deduction) {
        return mongo.save(deduction);
    }

    /**
     * Finds an InventoryDeduction record by its unique identifier. This method is essential for retrieving specific inventory deduction details based on the deduction ID, allowing for accurate tracking and management of inventory deductions in the system.
     *
     * @param id the unique identifier of the InventoryDeduction record to be retrieved
     * @return an Optional containing the InventoryDeduction record if found, or an empty Optional if no matching record exists
     */
    @Override
    public Optional<InventoryDeduction> findById(String id) {
        return mongo.findById(id);
    }

    /**
     * Finds a list of InventoryDeduction records associated with a specific branch ID. This method is essential for retrieving all inventory deduction records for a given branch, allowing for effective tracking and management of inventory deductions across different branches in the system.
     *
     * @param branchId the ID of the branch for which inventory deduction records are being retrieved
     * @return a list of InventoryDeduction records associated with the specified branch ID, or an empty list if no records are found
     */
    @Override
    public List<InventoryDeduction> findByBranchId(String branchId) {
        return mongo.findByBranchId(branchId);
    }
}
