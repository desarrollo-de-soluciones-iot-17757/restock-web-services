package com.uitopic.restock.platform.tracking.domain.repositories;

import com.uitopic.restock.platform.tracking.domain.model.aggregates.StockComparisonTask;

/**
 * Repository interface for managing stock comparison tasks, providing methods for saving and retrieving tasks related to inventory discrepancy tracking. This interface defines the contract for how stock comparison tasks are persisted and accessed within the system, allowing for flexibility in the underlying storage implementation.
 */
public interface StockComparisonTaskRepository {

    /**
     * Saves a stock comparison task to the repository, allowing for persistence and retrieval of the task for future processing. This method is used to store the task after it has been created or updated, ensuring that it can be accessed later when needed for comparison operations.
     *
     * @param task the stock comparison task to be saved, containing information about the task's parameters and status
     * @return the saved stock comparison task, potentially with updated information such as an assigned ID or timestamps after being persisted in the repository
     */
    StockComparisonTask save(StockComparisonTask task);
}
