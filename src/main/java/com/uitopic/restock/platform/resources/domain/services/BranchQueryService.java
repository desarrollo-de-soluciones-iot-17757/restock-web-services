package com.uitopic.restock.platform.resources.domain.services;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Branch;
import com.uitopic.restock.platform.resources.domain.model.queries.GetAllBranchesQuery;
import com.uitopic.restock.platform.resources.domain.model.queries.GetBranchByIdQuery;
import com.uitopic.restock.platform.resources.domain.model.queries.GetBranchesByAccountIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Query service contract for Branch read operations.
 */
public interface BranchQueryService {

    /**
     * Retrieves all branches .
     *
     * @return list of branches
     */
    List<Branch> handle(GetAllBranchesQuery query);

    /**
     * Retrieves a branch by its identifier.
     *
     * @param query query with the branch identifier
     * @return branch if found
     */
    Optional<Branch> handle(GetBranchByIdQuery query);

    /**
     * Retrieves all branches from an account.
     *
     * @param query query with the account identifier
     * @return list of branches for the account
     */
    List<Branch> handle(GetBranchesByAccountIdQuery query);

}