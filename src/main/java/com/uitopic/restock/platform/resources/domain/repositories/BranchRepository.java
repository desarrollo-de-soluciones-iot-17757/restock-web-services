package com.uitopic.restock.platform.resources.domain.repositories;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Branch;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;

import java.util.List;
import java.util.Optional;

/**
 * Domain repository port for {@link Branch} aggregate persistence within the resources bounded context.
 *
 * <p>Defines the contract for storing and retrieving branches, decoupling the domain
 * layer from the MongoDB infrastructure. The implementation is provided by
 * {@link com.uitopic.restock.platform.resources.infrastructure.repositories.BranchRepositoryImpl}.
 */
public interface BranchRepository {

    /**
     * Persists a {@link Branch} aggregate. Creates a new document if the branch has no ID,
     * or updates the existing document otherwise.
     *
     * @param branch the branch to save
     * @return the saved branch, including any auto-generated ID
     */
    Branch save(Branch branch);

    /**
     * Finds a branch by its unique identifier.
     *
     * @param id the unique identifier of the branch
     * @return an {@link Optional} containing the {@link Branch} if found, or empty if not found
     */
    Optional<Branch> findById(String id);

    /**
     * Finds all branches belonging to the specified account.
     *
     * @param accountId the account whose branches are to be retrieved
     * @return a {@link List} of {@link Branch} aggregates for that account
     */
    List<Branch> findByAccountId(AccountId accountId);

    /**
     * Removes a branch document from the store by its unique identifier.
     *
     * @param id the unique identifier of the branch to delete
     */
    void deleteById(String id);

    /**
     * Checks whether a branch with the given name already exists within the specified account.
     * Used to enforce name uniqueness per account before creating or renaming a branch.
     *
     * @param name      the branch name to check
     * @param accountId the account scope for the uniqueness check
     * @return {@code true} if a branch with that name exists in the account, {@code false} otherwise
     */
    boolean existsByNameAndAccountId(String name, AccountId accountId);
}
