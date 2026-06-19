package com.uitopic.restock.platform.tracking.application.internal.queryservices;

import com.uitopic.restock.platform.tracking.domain.model.aggregates.ConciliationTask;
import com.uitopic.restock.platform.tracking.domain.model.queries.GetConciliationTaskByIdQuery;
import com.uitopic.restock.platform.tracking.domain.model.queries.GetConciliationTasksByAccountIdQuery;
import com.uitopic.restock.platform.tracking.domain.model.queries.GetConciliationTasksByCustomSupplyIdQuery;
import com.uitopic.restock.platform.tracking.domain.repositories.ConciliationTaskRepository;
import com.uitopic.restock.platform.tracking.domain.services.ConciliationTaskQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Application query service for conciliation task read operations.
 *
 * <p>
 * Provides account-scoped list queries with optional filters and direct lookup
 * by conciliation task identifier. Filtering is delegated to the domain
 * repository so interfaces remain thin and focused on request mapping.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConciliationTaskQueryServiceImpl implements ConciliationTaskQueryService {
    private final ConciliationTaskRepository conciliationTaskRepository;

    /**
     * Retrieves conciliation tasks for an account using optional filters.
     *
     * @param query query with account identifier and optional filters
     * @return list of conciliation tasks matching the filters
     */
    @Override
    public List<ConciliationTask> handle(GetConciliationTasksByAccountIdQuery query) {
        log.debug("Querying conciliation tasks by accountId='{}'", query.accountId());
        return conciliationTaskRepository.findAllByAccountIdAndFilters(
                query.accountId(),
                query.status(),
                query.customSupplyId(),
                query.branchId(),
                query.deviceId()
        );
    }

    /**
     * Retrieves conciliation tasks by custom supply identifier.
     *
     * @param query query with custom supply identifier
     * @return list of conciliation tasks matching the custom supply identifier
     */
    @Override
    public List<ConciliationTask> handle(GetConciliationTasksByCustomSupplyIdQuery query) {
        log.debug("Querying conciliation tasks by customSupplyId='{}'", query.customSupplyId());
        return conciliationTaskRepository.findAllByCustomSupplyId(query.customSupplyId());
    }

    /**
     * Retrieves a conciliation task by its identifier.
     *
     * @param query query with conciliation task identifier
     * @return optional conciliation task if found
     */
    @Override
    public Optional<ConciliationTask> handle(GetConciliationTaskByIdQuery query) {
        log.debug("Querying conciliation task by id='{}'", query.conciliationTaskId());
        return conciliationTaskRepository.findById(query.conciliationTaskId());
    }
}
