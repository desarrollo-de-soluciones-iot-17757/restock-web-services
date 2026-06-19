package com.uitopic.restock.platform.tracking.domain.services;

import com.uitopic.restock.platform.tracking.domain.model.aggregates.ConciliationTask;
import com.uitopic.restock.platform.tracking.domain.model.queries.GetConciliationTaskByIdQuery;
import com.uitopic.restock.platform.tracking.domain.model.queries.GetConciliationTasksByAccountIdQuery;
import com.uitopic.restock.platform.tracking.domain.model.queries.GetConciliationTasksByCustomSupplyIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Domain service contract for conciliation task query operations.
 */
public interface ConciliationTaskQueryService {
    /**
     * Gets conciliation tasks for an account using optional filters.
     *
     * @param query query with account identifier and optional filters
     * @return list of matching conciliation tasks
     */
    List<ConciliationTask> handle(GetConciliationTasksByAccountIdQuery query);

    /**
     * Gets conciliation tasks by custom supply identifier.
     *
     * @param query query with custom supply identifier
     * @return list of matching conciliation tasks
     */
    List<ConciliationTask> handle(GetConciliationTasksByCustomSupplyIdQuery query);

    /**
     * Gets a conciliation task by its identifier.
     *
     * @param query query with conciliation task identifier
     * @return optional conciliation task if found
     */
    Optional<ConciliationTask> handle(GetConciliationTaskByIdQuery query);

}
