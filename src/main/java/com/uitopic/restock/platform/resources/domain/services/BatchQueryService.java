package com.uitopic.restock.platform.resources.domain.services;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Batch;
import com.uitopic.restock.platform.resources.domain.model.queries.GetAllBatchesQuery;
import com.uitopic.restock.platform.resources.domain.model.queries.GetBatchByIdQuery;
import com.uitopic.restock.platform.resources.domain.model.queries.GetBatchesByAccountIdQuery;
import com.uitopic.restock.platform.resources.domain.model.queries.GetBatchesByBranchIdQuery;
import com.uitopic.restock.platform.resources.domain.model.queries.GetBatchesByCustomSupplyIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Query service contract for Batch read operations.
 */
public interface BatchQueryService {

    /**
     * Retrieves all batches.
     *
     * @param query query to get all batches
     * @return list of batches
     */
    List<Batch> handle(GetAllBatchesQuery query);

    /**
     * Retrieves a batch by its identifier.
     *
     * @param query query with the batch identifier
     * @return batch if found
     */
    Optional<Batch> handle(GetBatchByIdQuery query);

    /**
     * Retrieves all batches from an account.
     *
     * @param query query with the account identifier
     * @return list of batches for the account
     */
    List<Batch> handle(GetBatchesByAccountIdQuery query);

    /**
     * Retrieves all batches from a branch.
     *
     * @param query query with the branch identifier
     * @return list of batches for the branch
     */
    List<Batch> handle(GetBatchesByBranchIdQuery query);

    /**
     * Retrieves all batches from a custom supply.
     *
     * @param query query with the custom supply identifier
     * @return list of batches for the custom supply
     */
    List<Batch> handle(GetBatchesByCustomSupplyIdQuery query);

    /**
     * Retrieves batches using optional filters.
     *
     * @param accountId optional account identifier
     * @param branchId optional branch identifier
     * @param customSupplyId optional custom supply identifier
     * @return list of batches matching the filters
     */
    List<Batch> findAllByFilters(String accountId, String branchId, String customSupplyId);
}