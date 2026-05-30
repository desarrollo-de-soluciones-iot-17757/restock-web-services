package com.uitopic.restock.platform.resources.domain.repositories;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Branch;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.Address;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Branch aggregates. This interface defines the contract for saving, retrieving, and deleting Branch entities from the underlying data store.
 * It includes methods for finding branches by their ID, finding all branches associated with a specific account, and checking for the existence of a branch by name within an account.
 */
public interface BranchRepository {

    /**
     * Saves a Branch entity to the repository. If the branch already exists, it will be updated; otherwise, a new entry will be created.
     *
     * @param branch the Branch entity to save
     * @return the saved Branch entity, which may include an auto-generated ID if it was newly created
     */
    Branch save(Branch branch);

    /**
     * Retrieves a Branch entity by its unique identifier.
     *
     * @param id the unique identifier of the Branch to retrieve
     * @return an Optional containing the Branch if found, or empty if no Branch with the given ID exists
     */
    Optional<Branch> findById(String id);

    /**
     * Retrieves a list of Branch entities associated with a specific account.
     *
     * @param accountId the unique identifier of the account whose branches are to be retrieved
     * @return a List of Branch entities associated with the specified account
     */
    List<Branch> findByAccountId(AccountId accountId);

    /**
     * Deletes a Branch entity from the repository by its unique identifier.
     *
     * @param id the unique identifier of the Branch to delete
     */
    void deleteById(String id);

    /**
     * Checks if a Branch with the given name already exists within the specified account.
     *
     * @param name the name of the Branch to check for existence
     * @param accountId the unique identifier of the account to which the Branch belongs
     * @return true if a Branch with the given name exists within the account, false otherwise
     */
    boolean existsByNameAndAccountId(String name, AccountId accountId);
}
