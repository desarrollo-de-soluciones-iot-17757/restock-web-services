package com.uitopic.restock.platform.resources.infrastructure.repositories;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Branch;
import com.uitopic.restock.platform.resources.domain.repositories.BranchRepository;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories.BranchMongoRepository;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.Address;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the BranchRepository interface using MongoDB as the underlying data store. This class uses a BranchMongoRepository to perform CRUD operations on Branch entities in the MongoDB database.
 * It provides methods for saving, retrieving, and deleting Branch entities, as well as checking for the existence of branches by name and location.
 */
@Repository
public class BranchRepositoryImpl implements BranchRepository {

    /** The MongoDB repository for managing Branch entities. */
    private final BranchMongoRepository mongo;

    /** Constructor for BranchRepositoryImpl. This constructor takes a BranchMongoRepository as a parameter and assigns it to the mongo field. The BranchMongoRepository is used to perform database operations on Branch entities. */
    public BranchRepositoryImpl(BranchMongoRepository mongo) {
        this.mongo = mongo;
    }

    /**
     * Saves a Branch entity to the database.
     * @param branch the Branch entity to save
     * @return the saved Branch entity
     */
    @Override
    public Branch save(Branch branch) {
        return mongo.save(branch);
    }

    /**
     * Retrieves a Branch entity by its unique identifier.
     * @param id the unique identifier of the Branch to retrieve
     * @return an Optional containing the Branch if found, or empty if no Branch with the given ID exists
     */
    @Override
    public Optional<Branch> findById(String id) {
        return mongo.findById(id);
    }

    /**
     * Retrieves a list of Branch entities associated with a specific account.
     * @param accountId the unique identifier of the account whose branches are to be retrieved
     * @return a List of Branch entities associated with the specified account
     */
    @Override
    public List<Branch> findByAccountId(AccountId accountId) {
        return mongo.findByAccountId(accountId);
    }

    /**
     * Deletes a Branch entity from the database by its unique identifier.
     * @param id the unique identifier of the Branch to delete
     */
    @Override
    public void deleteById(String id) {
        mongo.deleteById(id);
    }

    /**
     * Checks if a Branch with the given name already exists within the specified account.
     * @param name the name of the Branch to check for existence
     * @param accountId the unique identifier of the account to which the Branch belongs
     * @return true if a Branch with the given name exists within the account, false otherwise
     */
    @Override
    public boolean existsByNameAndAccountId(String name, AccountId accountId) {
        return mongo.existsByNameAndAccountId(name, accountId);
    }
}
