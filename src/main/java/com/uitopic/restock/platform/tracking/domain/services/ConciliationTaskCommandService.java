package com.uitopic.restock.platform.tracking.domain.services;

import com.uitopic.restock.platform.tracking.domain.model.aggregates.ConciliationTask;
import com.uitopic.restock.platform.tracking.domain.model.commands.ClosePendingConciliationTasksCommand;
import com.uitopic.restock.platform.tracking.domain.model.commands.CreateConciliationTaskCommand;
import com.uitopic.restock.platform.tracking.domain.model.commands.ResolveConciliationTaskCommand;

/**
 * Domain service contract for conciliation task command operations.
 *
 * <p>
 * Defines the operations required to create conciliation tasks from critical
 * discrepancies, resolve them manually, and close them automatically when stock
 * comparisons normalize.
 */
public interface ConciliationTaskCommandService {
    /**
     * Creates a pending conciliation task for a critical discrepancy.
     *
     * @param command command with the critical discrepancy
     * @return created conciliation task
     */
    ConciliationTask handle(CreateConciliationTaskCommand command);

    /**
     * Resolves a conciliation task using manual resolution data.
     *
     * @param command command containing task identifier and resolution details
     * @return resolved conciliation task
     */
    ConciliationTask handle(ResolveConciliationTaskCommand command);

    /**
     * Closes pending conciliation tasks in the same comparison scope when a
     * later comparison has no discrepancy.
     *
     * @param command command with the matching stock comparison task
     */
    void handle(ClosePendingConciliationTasksCommand command);
}
