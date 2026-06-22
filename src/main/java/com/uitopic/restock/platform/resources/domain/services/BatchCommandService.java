package com.uitopic.restock.platform.resources.domain.services;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Batch;
import com.uitopic.restock.platform.resources.domain.model.commands.*;

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

    /**
     * Subtracts or reduces stock from an existing batch.
     * Used for sales, consumption, or inventory shrinkage.
     *
     * @param command command with the batch identifier and quantity to subtract
     * @return the remaining stock level after the subtraction
     */
    double handle(SubtractBatchStockCommand command);

    /**
     * Adds back or returns stock to an existing batch.
     * Used for customer returns, cancellations, or inventory adjustments.
     *
     * @param command command with the batch identifier and quantity to add back
     * @return updated batch with the restored stock
     */
    Batch handle(AddBackBatchStockCommand command);

}