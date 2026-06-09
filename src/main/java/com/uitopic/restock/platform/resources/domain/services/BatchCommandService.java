package com.uitopic.restock.platform.resources.domain.services;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Batch;
import com.uitopic.restock.platform.resources.domain.model.commands.CreateBatchCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.DeleteBatchCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.TransferBatchStockCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.UpdateBatchCommand;

import java.util.List;
import java.util.Optional;

/**
 * Command service contract for Batch write operations.
 */
public interface BatchCommandService {

    /**
     * Creates a new batch.
     *
     * @param command command with the batch data
     * @return created batch
     */
    Batch handle(CreateBatchCommand command);

    /**
     * Updates an existing batch.
     *
     * Entry date is not updated because it represents when the batch was registered.
     *
     * @param command command with the updated batch data
     * @return updated batch, or empty if not found
     */
    Optional<Batch> handle(UpdateBatchCommand command);

    /**
     * Deletes an existing batch.
     *
     * @param command command with the batch identifier
     */
    void handle(DeleteBatchCommand command);

    /**
     * Transfers stock from a batch to another branch.
     *
     * @param command command with source batch, target branch and quantity
     * @return affected batches: source and target
     */
    List<Batch> handle(TransferBatchStockCommand command);
    
}