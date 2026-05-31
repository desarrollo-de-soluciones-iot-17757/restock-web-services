package com.uitopic.restock.platform.resources.domain.services;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Batch;
import com.uitopic.restock.platform.resources.domain.model.commands.CreateBatchCommand;

import java.util.Optional;

/**
 * Service interface for handling batch-related commands, including creating batches, transferring inventory, and subtracting inventory.
 * This service provides methods to process commands and manage inventory levels across branches, ensuring accurate stock management and traceability of inventory movements.
 */
public interface BatchCommandService {

    /**
     * Handles the creation of a new batch based on the provided command, which includes details such as the supply, quantity, and associated branch.
     *
     * @param command the command containing the necessary information to create a new batch
     * @return an Optional containing the created Batch if successful, or an empty Optional if the creation failed due to validation errors or other issues
     */
    Optional<Batch> handle(CreateBatchCommand command);
}
