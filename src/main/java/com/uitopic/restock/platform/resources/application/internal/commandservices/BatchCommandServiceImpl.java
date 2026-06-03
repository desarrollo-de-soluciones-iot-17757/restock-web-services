package com.uitopic.restock.platform.resources.application.internal.commandservices;

import com.uitopic.restock.platform.resources.domain.exception.BatchNotFoundException;
import com.uitopic.restock.platform.resources.domain.exception.BranchNotFoundException;
import com.uitopic.restock.platform.resources.domain.exception.CustomSupplyNotFoundException;
import com.uitopic.restock.platform.resources.domain.model.aggregates.Batch;
import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.resources.domain.model.commands.CreateBatchCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.DeleteBatchCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.TransferBatchStockCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.UpdateBatchCommand;
import com.uitopic.restock.platform.resources.domain.model.events.StockTransferredEvent;
import com.uitopic.restock.platform.resources.domain.model.valueobjects.Stock;
import com.uitopic.restock.platform.resources.domain.repositories.BatchRepository;
import com.uitopic.restock.platform.resources.domain.repositories.BranchRepository;
import com.uitopic.restock.platform.resources.domain.repositories.CustomSupplyRepository;
import com.uitopic.restock.platform.resources.domain.services.BatchCommandService;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.infrastructure.eventpublisher.spring.SpringDomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Application service for Batch write operations.
 * Handles batch creation, update and deletion.
 * Inventory transfer and stock subtraction are intentionally left out for now.
 */
@Slf4j
@Service
public class BatchCommandServiceImpl implements BatchCommandService {

    // The batch repository is used for all batch operations, including transfers, to ensure consistency and proper event handling.
    private final BatchRepository batchRepository;

    // The branch repository is used to validate branch existence and ownership during batch creation and transfer operations.
    private final BranchRepository branchRepository;

    // The event publisher is used to publish domain events, such as StockTransferredEvent, after successful operations that modify the batch state, ensuring that other parts of the system can react to these changes.
    private final SpringDomainEventPublisher eventPublisher;

    // The custom supply repository is used to validate the existence of the custom supply and to retrieve its details, such as unit measurement and perishability, during batch creation, update and transfer operations.
    private final CustomSupplyRepository customSupplyRepository;

    public BatchCommandServiceImpl(
            BatchRepository batchRepository,
            BranchRepository branchRepository,
            CustomSupplyRepository customSupplyRepository,
            SpringDomainEventPublisher eventPublisher
    ) {
        this.batchRepository = batchRepository;
        this.branchRepository = branchRepository;
        this.customSupplyRepository = customSupplyRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Creates a new batch.
     *
     * @param command command with the batch data
     * @return created batch
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Batch handle(CreateBatchCommand command) {
        log.info(
                "Creating batch '{}' for customSupplyId='{}' and branchId='{}'",
                command.code(),
                command.customSupplyId(),
                command.branchId()
        );

        validateBranchExists(command.branchId());
        validateBranchBelongsToAccount(command.branchId(), command.accountId());
        CustomSupply customSupply = findCustomSupplyOrThrow(command.customSupplyId());
        validateExpirationDate(customSupply, command.expirationDate());
        validateStockRange(command.currentStock(), customSupply);

        Stock currentStock = new Stock(
                command.currentStock(),
                customSupply.getUnitMeasurement()
        );

        Batch batch = new Batch(
                command.code(),
                currentStock,
                command.customSupplyId(),
                command.branchId(),
                command.accountId(),
                command.expirationDate(),
                command.entryDate()
        );

        Batch saved = batchRepository.save(batch);
        log.info("Batch created successfully: id='{}'", saved.getId());
        return saved;
    }

    /**
     * Partially updates an existing batch.
     * <p>
     * Only provided fields are applied. The custom supply, branch and entry date
     * are not changed from this endpoint.
     *
     * @param command command with optional batch fields
     * @return updated batch, or empty if not found
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Optional<Batch> handle(UpdateBatchCommand command) {
        log.info("Updating batch id='{}'", command.batchId());

        return batchRepository.findById(command.batchId()).map(batch -> {
            CustomSupply customSupply = findCustomSupplyOrThrow(batch.getCustomSupplyId());
            LocalDate finalExpirationDate = command.expirationDate() != null
                    ? command.expirationDate()
                    : batch.getExpirationDate();
            validateExpirationDate(customSupply, finalExpirationDate);

            if (command.code() != null && !command.code().isBlank()) {
                batch.changeCode(command.code());
            }

            if (command.currentStock() != null) {
                validateStockRange(command.currentStock(), customSupply);

                batch.changeCurrentStock(new Stock(
                        command.currentStock(),
                        customSupply.getUnitMeasurement()
                ));
            }

            if (command.expirationDate() != null) {
                batch.changeExpirationDate(command.expirationDate());
            }

            Batch updated = batchRepository.save(batch);
            log.info("Batch updated successfully: id='{}'", updated.getId());
            return updated;
        });
    }

    /**
     * Deletes a batch.
     *
     * @param command command with the batch identifier
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handle(DeleteBatchCommand command) {
        log.info("Deleting batch id='{}'", command.batchId());
        if (batchRepository.findById(command.batchId()).isEmpty()) {
            throw new IllegalArgumentException("Batch not found: " + command.batchId());
        }
        batchRepository.deleteById(command.batchId());
        log.info("Batch deleted successfully: id='{}'", command.batchId());
    }

    /**
     * Validates that a branch exists by its identifier.
     *
     * @param branchId branch identifier to validate
     */
    private void validateBranchExists(String branchId) {
        if (branchRepository.findById(branchId).isEmpty()) {
            throw new IllegalArgumentException("Branch not found: " + branchId);
        }
    }

    /**
     * Transfers stock from a source batch to another branch.
     * <p>
     * If the custom supply is perishable, the target batch is matched by code,
     * custom supply, branch and expiration date. If it is not perishable, the
     * target batch is matched by custom supply and branch only.
     *
     * @param command command with source batch, target branch and quantity
     * @return affected batches: source and target
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Batch> handle(TransferBatchStockCommand command) {
        log.info(
                "Transferring stock from batch id='{}' to branch id='{}'",
                command.sourceBatchId(),
                command.targetBranchId()
        );

        Batch sourceBatch = batchRepository.findById(command.sourceBatchId())
                .orElseThrow(() -> new BatchNotFoundException(
                        "Source batch not found: " + command.sourceBatchId()
                ));

        var targetBranch = branchRepository.findById(command.targetBranchId())
                .orElseThrow(() -> new BranchNotFoundException(
                        "Target branch not found: " + command.targetBranchId()
                ));

        if (!sourceBatch.getAccountId().equals(targetBranch.getAccountId())) {
            throw new IllegalArgumentException("Source batch and target branch must belong to the same account");
        }

        if (sourceBatch.getBranchId().equals(command.targetBranchId())) {
            throw new IllegalArgumentException("Source batch and target branch must be different");
        }

        CustomSupply customSupply = customSupplyRepository.findById(sourceBatch.getCustomSupplyId())
                .orElseThrow(() -> new CustomSupplyNotFoundException(
                        "Custom supply not found: " + sourceBatch.getCustomSupplyId()
                ));

        boolean isPerishable = customSupply.getSupply() != null
                && Boolean.TRUE.equals(customSupply.getSupply().getIsPerishable());

        var quantity = command.quantity();

        var targetBatch = findCompatibleTargetBatch(sourceBatch, command.targetBranchId(), isPerishable)
                .map(existingTargetBatch -> {
                    existingTargetBatch.increase(quantity);
                    return existingTargetBatch;
                })
                .orElseGet(() -> createTargetBatchFromTransfer(sourceBatch, command, quantity));

        sourceBatch.subtract(quantity);

        // Create a domain event to represent the stock transfer, capturing relevant information about the source and target batches, the quantity transferred, and the remaining stock at both branches after the transfer. This event can be used to notify other parts of the system about the inventory change and trigger any necessary actions or updates.
        var stockTransferredEvent = StockTransferredEvent.builder()
                .fromBatchId(sourceBatch.getId())
                .fromBranchRemainingStock(sourceBatch.getCurrentStock().stock())
                .toBranchId(targetBatch.getBranchId())
                .toBranchRemainingStock(targetBatch.getCurrentStock().stock() + quantity.stock())
                .quantityTransferred(quantity.stock())
                .unitMeasurement(quantity.unitMeasurement().unitName())
                .build();

        // Register the domain event on the source batch, so it will be published after the batch is saved. This allows for proper event handling and ensures that the event is only published if the transfer operation is successful and the batch state is updated accordingly.
        sourceBatch.registerDomainEvent(stockTransferredEvent);

        Batch savedSource = batchRepository.save(sourceBatch);
        Batch savedTarget = batchRepository.save(targetBatch);

        // Publish a domain event after the transfer is successful, allowing other parts of the system to react to the stock change.
        eventPublisher.publish(stockTransferredEvent);

        // Clear domain events after publishing to prevent duplicate events in case of retries or multiple operations on the same batch within the same transaction.
        sourceBatch.clearDomainEvents();

        log.info(
                "Stock transferred successfully from batch id='{}' to batch id='{}'",
                savedSource.getId(),
                savedTarget.getId()
        );

        return List.of(savedSource, savedTarget);
    }

    /**
     * Finds a compatible target batch for the transfer.
     *
     * @param sourceBatch source batch
     * @param targetBranchId target branch identifier
     * @param isPerishable whether the custom supply is perishable
     * @return compatible target batch if found
     */
    private Optional<Batch> findCompatibleTargetBatch(
            Batch sourceBatch,
            String targetBranchId,
            boolean isPerishable
    ) {
        if (isPerishable) {
            return batchRepository.findFirstByCodeAndCustomSupplyIdAndBranchIdAndExpirationDate(
                    sourceBatch.getCode(),
                    sourceBatch.getCustomSupplyId(),
                    targetBranchId,
                    sourceBatch.getExpirationDate()
            );
        }

        return batchRepository.findFirstByBranchIdAndCustomSupplyId(
                targetBranchId,
                sourceBatch.getCustomSupplyId()
        );
    }

    /**
     * Creates a target batch from a transfer operation.
     * <p>
     * The new batch keeps the same code, custom supply and expiration date from
     * the source batch. The entry date is the transfer date.
     *
     * @param sourceBatch source batch
     * @param command transfer command
     * @return new target batch
     */
    private Batch createTargetBatchFromTransfer(
            Batch sourceBatch,
            TransferBatchStockCommand command,
            Stock quantity
    ) {
        return new Batch(
                sourceBatch.getCode(),
                quantity,
                sourceBatch.getCustomSupplyId(),
                command.targetBranchId(),
                sourceBatch.getAccountId().getAccountId(),
                sourceBatch.getExpirationDate(),
                LocalDate.now()
        );
    }

    /**
     * Creates a stock value object using the official unit measurement of a custom supply.
     *
     * @param quantity stock quantity
     * @param customSupply custom supply that defines the unit of measurement
     * @return stock with the custom supply unit
     */
    private Stock createStockFromCustomSupply(Double quantity, CustomSupply customSupply) {
        return new Stock(quantity, customSupply.getUnitMeasurement());
    }

    /**
     * Validates expiration date rules according to the custom supply perishability.
     *
     * @param customSupply custom supply associated with the batch
     * @param expirationDate expiration date to validate
     */
    private void validateExpirationDate(CustomSupply customSupply, LocalDate expirationDate) {
        boolean isPerishable = customSupply.getSupply() != null
                && Boolean.TRUE.equals(customSupply.getSupply().getIsPerishable());

        if (isPerishable && expirationDate == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Expiration date is required for perishable supplies"
            );
        }
    }

    /**
     * Finds a custom supply by ID.
     *
     * @param customSupplyId custom supply identifier
     * @return custom supply if found
     */
    private CustomSupply findCustomSupplyOrThrow(String customSupplyId) {
        return customSupplyRepository.findById(customSupplyId)
                .orElseThrow(() -> new CustomSupplyNotFoundException(
                        "Custom supply not found: " + customSupplyId
                ));
    }

    /**
     * Validates that the stock value is within the custom supply stock range.
     *
     * @param currentStock current stock quantity
     * @param customSupply custom supply associated with the batch
     */
    private void validateStockRange(Double currentStock, CustomSupply customSupply) {
        if (currentStock == null) {
            return;
        }

        if (customSupply.getStockRange() == null) {
            return;
        }

        if (!customSupply.getStockRange().isInRange(currentStock)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Current stock must be between "
                            + customSupply.getStockRange().minStock()
                            + " and "
                            + customSupply.getStockRange().maxStock()
            );
        }
    }

    /**
     * Finds a branch and validates that it belongs to the account.
     *
     * @param branchId branch identifier
     * @param accountId account identifier
     */
    private void validateBranchBelongsToAccount(String branchId, String accountId) {
        var branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new BranchNotFoundException(
                        "Branch not found: " + branchId
                ));

        if (!branch.getAccountId().equals(new AccountId(accountId))) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Branch does not belong to the provided account"
            );
        }
    }
}