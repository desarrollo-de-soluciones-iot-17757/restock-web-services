package com.uitopic.restock.platform.resources.domain.services;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Branch;
import com.uitopic.restock.platform.resources.domain.model.queries.GetBranchesByAccountIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Domain service interface defining the query contract for {@link Branch} aggregate retrieval.
 *
 * <p>Declares the read-side operations available on branches: lookup by ID and listing
 * by account with optional state filtering and pagination. Implementations live in the
 * application layer
 * ({@link com.uitopic.restock.platform.resources.application.internal.queryservices.BranchQueryServiceImpl}).
 */
public interface BranchQueryService {

    /**
     * Finds a single branch by its unique identifier.
     *
     * @param id the unique identifier of the branch
     * @return an {@link Optional} containing the {@link Branch} if found, or empty if not found
     */
    Optional<Branch> findById(String id);

    /**
     * Retrieves all branches associated with the account specified in the query.
     *
     * @param query the query containing the account ID
     * @return a {@link List} of {@link Branch} aggregates for that account
     */
    List<Branch> handle(GetBranchesByAccountIdQuery query);

    /**
     * Retrieves a paginated and optionally filtered list of branches for the account
     * specified in the query.
     *
     * @param query the query containing the account ID
     * @param state optional status filter (e.g., {@code "ACTIVE"} or {@code "INACTIVE"}); pass {@code null} or blank to return all
     * @param page  zero-based page index
     * @param size  maximum number of results per page
     * @return a {@link List} of {@link Branch} aggregates matching the filter and page window
     */
    List<Branch> handle(GetBranchesByAccountIdQuery query, String state, int page, int size);
}
