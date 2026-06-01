package com.uitopic.restock.platform.resources.domain.services;

import com.uitopic.restock.platform.resources.domain.model.commands.SubtractInventoryCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.TransferInventoryCommand;
import com.uitopic.restock.platform.resources.domain.model.entities.Inventory;

import java.util.Optional;

/**
 * Service interface for handling inventory-related commands, including transferring inventory between branches and subtracting inventory from a branch. This service provides methods to process commands and manage inventory levels across branches, ensuring accurate stock management and traceability of inventory movements.
 */
public interface InventoryCommandService {

    /**
     * Handles the transfer of inventory from one branch to another based on the provided command, which includes details such as the source branch, destination branch, supply, and quantity.
     *
     * @param command the command containing the necessary information to transfer inventory between branches
     * @return an Optional containing the InventoryTransfer record if the transfer was successful, or an empty Optional if the transfer failed due to validation errors, insufficient stock, or other issues
     */
    Optional<Inventory> handle(TransferInventoryCommand command);

    /**
     * Handles the subtraction of inventory from a branch based on the provided command, which includes details such as the branch, supply, and quantity to be subtracted.
     *
     * @param command the command containing the necessary information to subtract inventory from a branch
     * @return an Optional containing the InventoryDeduction record if the subtraction was successful, or an empty Optional if the subtraction failed due to validation errors, insufficient stock, or other issues
     */
    Optional<Inventory> handle(SubtractInventoryCommand command);
}
