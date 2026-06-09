package com.uitopic.restock.platform.resources.domain.repositories;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Branch;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;

import java.util.List;
import java.util.Optional;

/**
 * Domain repository port for Branch aggregate persistence.
 */
public interface BranchRepository {

    /**
     * Find all branches.
     *
     * @return list of branches
     */
    List<Branch> findAll();

    /**
     * Saves a branch.
     *
     * @param branch branch to save
     * @return saved branch
     */
    Branch save(Branch branch);

    /**
     * Finds a branch by its identifier.
     *
     * @param id branch identifier
     * @return branch if found
     */
    Optional<Branch> findById(String id);

    /**
     * Finds all branches that belong to an account.
     *
     * @param accountId account identifier
     * @return branches owned by the account
     */
    List<Branch> findByAccountId(AccountId accountId);

    /**
     * Checks whether a branch name already exists in an account.
     *
     * @param name branch name
     * @param accountId account identifier
     * @return true if a branch with that name exists
     */
    boolean existsByNameAndAccountId(String name, AccountId accountId);

    /**
     * Checks whether another branch with the same name exists in an account,
     * excluding the current branch.
     *
     * @param name branch name
     * @param accountId account identifier
     * @param id branch identifier to exclude
     * @return true if another branch with the same name exists
     */
    boolean existsByNameAndAccountIdAndIdNot(String name, AccountId accountId, String id);
}