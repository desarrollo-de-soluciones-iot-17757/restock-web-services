package com.uitopic.restock.platform.tracking.domain.repositories;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.BatchId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.BranchId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.DeviceId;
import com.uitopic.restock.platform.tracking.domain.model.aggregates.ConciliationTask;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.ConciliationTaskStatus;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing conciliation task aggregates.
 *
 * <p>
 * Provides persistence operations and query methods required by the tracking
 * bounded context to list, resolve and automatically close conciliation tasks.
 */
public interface ConciliationTaskRepository {
    /**
     * Saves a conciliation task aggregate.
     *
     * @param conciliationTask conciliation task to persist
     * @return persisted conciliation task
     */
    ConciliationTask save(ConciliationTask conciliationTask);

    /**
     * Finds a conciliation task by its identifier.
     *
     * @param id conciliation task identifier
     * @return optional conciliation task if found
     */
    Optional<ConciliationTask> findById(String id);

    /**
     * Finds all conciliation tasks for an account.
     *
     * @param accountId account identifier
     * @return list of conciliation tasks owned by the account
     */
    List<ConciliationTask> findAllByAccountId(AccountId accountId);

    /**
     * Finds conciliation tasks for an account using optional filters.
     *
     * @param accountId account identifier
     * @param status optional conciliation task status
     * @param customSupplyId optional custom supply identifier
     * @param branchId optional branch identifier
     * @param deviceId optional device identifier
     * @return list of matching conciliation tasks
     */
    List<ConciliationTask> findAllByAccountIdAndFilters(
            AccountId accountId,
            ConciliationTaskStatus status,
            String customSupplyId,
            BranchId branchId,
            DeviceId deviceId
    );

    /**
     * Finds pending conciliation tasks for the same inventory comparison scope.
     *
     * @param accountId account identifier
     * @param deviceId device identifier
     * @param customSupplyId custom supply identifier
     * @param batchId batch identifier
     * @return pending conciliation tasks matching the scope
     */
    List<ConciliationTask> findPendingByScope(AccountId accountId, DeviceId deviceId, String customSupplyId, BatchId batchId);
}
