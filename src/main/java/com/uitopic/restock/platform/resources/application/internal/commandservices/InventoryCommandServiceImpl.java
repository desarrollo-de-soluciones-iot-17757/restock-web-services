package com.uitopic.restock.platform.resources.application.internal.commandservices;

import com.uitopic.restock.platform.resources.domain.exception.*;
import com.uitopic.restock.platform.resources.domain.model.commands.SubtractInventoryCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.TransferInventoryCommand;
import com.uitopic.restock.platform.resources.domain.model.entities.Inventory;
import com.uitopic.restock.platform.resources.domain.model.entities.InventoryDeduction;
import com.uitopic.restock.platform.resources.domain.model.entities.InventoryTransfer;
import com.uitopic.restock.platform.resources.domain.model.valueobjects.Stock;
import com.uitopic.restock.platform.resources.domain.repositories.*;
import com.uitopic.restock.platform.resources.domain.services.InventoryCommandService;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.UnitMeasurement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

/**
 * Implementation of InventoryCommandService that handles inventory-related commands, including transferring inventory between branches and subtracting inventory from a branch. This service provides methods to process commands and manage inventory levels across branches, ensuring accurate stock management and traceability of inventory movements.
 */
@Slf4j
@Transactional
@Service
public class InventoryCommandServiceImpl implements InventoryCommandService {

    // Repositories for accessing inventory and branch data
    private final InventoryRepository inventoryRepository;

    // Repository for accessing branch data, used for validating branch existence and retrieving account information
    private final BranchRepository branchRepository;

    // Repository for managing inventory transfer records, used to save transfer details and track inventory movements between branches
    private final BatchRepository batchRepository;

    // Repository for managing inventory deduction records, used to save details of inventory subtractions and track inventory movements when stock is subtracted from a branch
    private final InventoryDeductionRepository inventoryDeductionRepository;

    // Repository for managing inventory transfer records, used to save details of inventory transfers and track inventory movements between branches
    private final InventoryTransferRepository inventoryTransferRepository;

    // Constructor for dependency injection of repositories
    public InventoryCommandServiceImpl(InventoryRepository inventoryRepository,
                                       BranchRepository branchRepository,
                                       BatchRepository batchRepository,
                                       InventoryDeductionRepository inventoryDeductionRepository,
                                       InventoryTransferRepository inventoryTransferRepository) {
        this.inventoryRepository = inventoryRepository;
        this.branchRepository = branchRepository;
        this.batchRepository = batchRepository;
        this.inventoryDeductionRepository = inventoryDeductionRepository;
        this.inventoryTransferRepository = inventoryTransferRepository;
    }

    /**
     * Handles the transfer of inventory from one branch to another based on the provided command, which includes details such as the source branch, destination branch, supply, and quantity.
     *
     * @param command the command containing the necessary information to transfer inventory between branches
     * @return an Optional containing the InventoryTransfer record if the transfer was successful, or an empty Optional if the transfer failed due to validation errors, insufficient stock, or other issues
     */
    @Override
    @Transactional
    public Optional<Inventory> handle(TransferInventoryCommand command) {

        // Validate that the source and destination branches are not the same. If they are the same, log a warning message with details about the branch ID and throw a TransferActionFailedException with an appropriate message indicating that the source and destination branches cannot be the same.
        if (command.fromBranchId().equals(command.toBranchId())) {
            log.warn("Attempted to transfer inventory from branch {} to itself", command.fromBranchId());
            throw new TransferActionFailedException("Source and destination branches cannot be the same");
        }

        // Retrieve the batch to be moved based on the batch ID provided in the command. If the batch is not found, throw a BatchNotFoundException with an appropriate message.
        var batchToMove = batchRepository
                .findById(command.batchId())
                .orElseThrow(() -> new BatchNotFoundException("Batch not found with ID: " + command.batchId()));

        // Retrieve the source branch based on the fromBranchId provided in the command. If the branch is not found, throw a BranchNotFoundException with an appropriate message.
        var fromBranch = branchRepository
                .findById(command.fromBranchId())
                .orElseThrow(() -> new BranchNotFoundException("Source branch not found with ID: " + command.fromBranchId()));

        // Retrieve the destination branch based on the toBranchId provided in the command. If the branch is not found, throw a BranchNotFoundException with an appropriate message.
        var toBranch = branchRepository
                .findById(command.toBranchId())
                .orElseThrow(() -> new BranchNotFoundException("Destination branch not found with ID: " + command.toBranchId()));

        // Validate that the source and destination branches belong to the same account. If they belong to different accounts, log a warning message with details about the branch IDs and throw a TransferActionFailedException with an appropriate message indicating that branches belong to different accounts.
        if (!fromBranch.getAccountId().equals(toBranch.getAccountId())) {
            log.warn("Attempted to transfer inventory between branches belonging to different accounts: fromBranchId={}, toBranchId={}", command.fromBranchId(), command.toBranchId());
            throw new TransferActionFailedException("Branches belong to different accounts");
        }

        // Get the current inventory for the specified batch and source branch. If the inventory is not found, throw an InventoryNotFoundException with an appropriate message.
        var currentInventory = inventoryRepository
                .findByBranchIdAndBatchId(command.batchId(), command.fromBranchId())
                .orElseThrow(() -> new InventoryNotFoundException("Inventory not found for batch ID: " + command.batchId() + " and branch ID: " + command.fromBranchId()));

        // Subtract the specified quantity from the current inventory. If the subtraction results in a negative stock level, throw an InventoryNotFoundException with an appropriate message indicating insufficient stock.
        try {
            currentInventory.subtrack(command.quantity());
        } catch (InvalidStockException e) {
            log.error("Failed to subtract stock for batch ID: {} and branch ID: {}. Reason: {}", command.batchId(), command.fromBranchId(), e.getMessage());
            throw new TransferActionFailedException("Failed to subtract stock: " + e.getMessage());
        }

        // Save the updated inventory after subtraction. If the save operation fails, log an error message with details about the batch ID and branch ID, and throw a RuntimeException with an appropriate message.
        try {
            inventoryRepository.save(currentInventory);
        } catch (Exception e) {
            log.error("Failed to save updated inventory for batch ID: {} and branch ID: {}. Reason: {}", command.batchId(), command.fromBranchId(), e.getMessage());
            throw new TransferActionFailedException("Failed to update inventory after subtraction");
        }

        // Retrieve or create the destination inventory for the specified batch and destination branch. If the retrieval or creation fails, log an error message with details about the batch ID and branch ID, and throw a RuntimeException with an appropriate message.
        Inventory destinationInventory;
        try {
            destinationInventory = inventoryRepository
                    .findByBranchIdAndBatchId(command.batchId(), command.toBranchId())
                    .orElseGet(() -> Inventory.builder()
                            .branchId(command.toBranchId())
                            .batchId(command.batchId())
                            .currentStock(new Stock(0.0, currentInventory.getCurrentStock().unitMeasurement()))
                            .build());
        } catch (Exception e) {
            log.error("Failed to retrieve or create destination inventory for batch ID: {} and branch ID: {}. Reason: {}", command.batchId(), command.toBranchId(), e.getMessage());
            throw new TransferActionFailedException("Failed to retrieve or create destination inventory");
        }

        // Add the specified quantity to the destination inventory. If the addition fails due to an InvalidStockException, log an error message with details about the batch ID and branch ID, and throw a RuntimeException with an appropriate message.
        try {
            destinationInventory.add(new Stock(command.quantity().getValue(), currentInventory.getCurrentStock().unitMeasurement()));
        } catch (InvalidStockException e) {
            log.error("Failed to add stock to destination inventory for batch ID: {} and branch ID: {}. Reason: {}", command.batchId(), command.toBranchId(), e.getMessage());
            throw new TransferActionFailedException("Failed to add stock to destination inventory: " + e.getMessage());
        }

        // Save the updated destination inventory. If the save operation fails, log an error message with details about the batch ID and branch ID, and throw a RuntimeException with an appropriate message.
        inventoryRepository.save(destinationInventory);

        // Create an inventory transfer record to log the transfer of inventory between branches. This record includes details such as the source branch ID, destination branch ID, quantity transferred, unit of measurement, reason for transfer, and timestamp. This allows for traceability and auditing of inventory movements between branches.
        InventoryTransfer transferRecord;
        try {
            transferRecord = InventoryTransfer.builder()
                    .fromBranchId(command.fromBranchId())
                    .fromBranchCurrentStock(currentInventory.getCurrentStock().getValue())
                    .toBranchId(command.toBranchId())
                    .toBranchCurrentStock(currentInventory.getCurrentStock().getValue())
                    .quantityTransferred(command.quantity().getValue())
                    .unit(new UnitMeasurement(command.quantity().unitMeasurement()))
                    .reason(command.reason())
                    .timestamp(String.valueOf(Instant.now()))
                    .build();
        } catch (Exception e) {
            log.error("Failed to create inventory transfer record for batch ID: {}. Reason: {}", command.batchId(), e.getMessage());
            throw new TransferActionFailedException("Failed to create inventory transfer record");
        }

        // Save the inventory transfer record to the repository. If the save operation fails, log an error message with details about the batch ID, source branch ID, and destination branch ID, and throw a RuntimeException with an appropriate message.
        try {
            inventoryTransferRepository.save(transferRecord);
        } catch (Exception e) {
            log.error("Failed to save inventory transfer record for batch ID: {}. Reason: {}", command.batchId(), e.getMessage());
            throw new TransferActionFailedException("Failed to save inventory transfer record");
        }

        // Returns an Optional containing the updated current inventory after the transfer operation is completed successfully. This allows the caller to access the updated inventory details if needed.
        return Optional.of(currentInventory);
    }

    /**
     * Handles the subtraction of inventory from a branch based on the provided command, which includes details such as the branch, supply, and quantity to be subtracted.
     *
     * @param command the command containing the necessary information to subtract inventory from a branch
     * @return an Optional containing the InventoryDeduction record if the subtraction was successful, or an empty Optional if the subtraction failed due to validation errors, insufficient stock, or other issues
     */
    @Override
    @Transactional
    public Optional<Inventory> handle(SubtractInventoryCommand command) {

        // Retrieve the batch to be moved based on the batch ID provided in the command. If the batch is not found, throw a BatchNotFoundException with an appropriate message.
        var batchToSubtrack = batchRepository
                .findById(command.batchId())
                .orElseThrow(() -> new BatchNotFoundException("Batch not found with ID: " + command.batchId()));

        // Retrieve the source branch based on the fromBranchId provided in the command. If the branch is not found, throw a BranchNotFoundException with an appropriate message.
        var fromBranch = branchRepository
                .findById(command.branchId())
                .orElseThrow(() -> new BranchNotFoundException("Source branch not found with ID: " + command.branchId()));

        // Get the current inventory for the specified batch and source branch. If the inventory is not found, throw an InventoryNotFoundException with an appropriate message.
        var inventory = inventoryRepository
                .findByBranchIdAndBatchId(command.batchId(), command.branchId())
                .orElseThrow(() -> new InventoryNotFoundException("Inventory not found for batch ID: " + command.batchId() + " and branch ID: " + command.branchId()));

        // Subtract the specified quantity from the current inventory. If the subtraction results in a negative stock level, throw an InventoryNotFoundException with an appropriate message indicating insufficient stock.
        try {
            inventory.subtrack(command.quantity());
        } catch (InvalidStockException e) {
            log.error("Failed to subtract stock for inventory with batch ID: {} and branch ID: {}. Reason: {}", command.batchId(), command.branchId(), e.getMessage());
            throw new SubtrackActionFailedException("Failed to subtract stock: " + e.getMessage());
        }

        // Save the updated inventory after subtraction. If the save operation fails, log an error message with details about the batch ID and branch ID, and throw a RuntimeException with an appropriate message.
        try {
            inventoryRepository.save(inventory);
        } catch (Exception e) {
            log.error("Failed to update inventory for batch ID: {} and branch ID: {}. Reason: {}", command.batchId(), command.branchId(), e.getMessage());
            throw new SubtrackActionFailedException("Failed to update inventory after subtraction");
        }

        InventoryDeduction deductionRecord;
        try {
            // Create an inventory deduction record to log the subtraction of inventory. This record includes details such as the branch ID, batch ID, quantity subtracted, unit of measurement, reason for subtraction, timestamp, and remaining stock after the subtraction. This allows for traceability and auditing of inventory movements.
            deductionRecord = InventoryDeduction.builder()
                    .branchId(command.branchId())
                    .batchId(command.batchId())
                    .quantity(command.quantity().getValue())
                    .unit(new UnitMeasurement(command.quantity().unitMeasurement()))
                    .reason(command.reason())
                    .timestamp(String.valueOf(Instant.now()))
                    .remainingStock(inventory.getCurrentStock().getValue())
                    .build();
        } catch (Exception e) {
            log.error("Failed to create inventory deduction record for batch ID: {} and branch ID: {}. Reason: {}", command.batchId(), command.branchId(), e.getMessage());
            throw new SubtrackActionFailedException("Failed to create inventory deduction record");
        }

        // Save the inventory deduction record to the repository. If the save operation fails, log an error message with details about the batch ID and branch ID, and throw a RuntimeException with an appropriate message.
        try {
            inventoryDeductionRepository.save(deductionRecord);
        } catch (Exception e) {
            log.error("Failed to save inventory deduction record for batch ID: {} and branch ID: {}. Reason: {}", command.batchId(), command.branchId(), e.getMessage());
            throw new SubtrackActionFailedException("Failed to save inventory deduction record");
        }

        // Returns an Optional containing the updated current inventory after the transfer operation is completed successfully. This allows the caller to access the updated inventory details if needed.
        return Optional.of(inventory);
    }
}
