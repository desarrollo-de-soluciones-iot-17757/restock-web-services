package com.uitopic.restock.platform.resources.application.internal.commandservices;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Batch;
import com.uitopic.restock.platform.resources.domain.model.commands.CreateBatchCommand;
import com.uitopic.restock.platform.resources.domain.model.events.StockIncreasedEvent;
import com.uitopic.restock.platform.resources.domain.repositories.BatchRepository;
import com.uitopic.restock.platform.resources.domain.repositories.BranchRepository;
import com.uitopic.restock.platform.resources.domain.repositories.InventoryDeductionRepository;
import com.uitopic.restock.platform.resources.domain.repositories.InventoryTransferRepository;
import com.uitopic.restock.platform.resources.domain.services.BatchCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementation of BatchCommandService that handles inventory operations such as creating batches, transferring inventory, and subtracting inventory.
 * This service ensures that inventory operations are performed in a FIFO manner and that appropriate events are published for each operation.
 */
@Slf4j
@Service
@Transactional
public class BatchCommandServiceImpl implements BatchCommandService {

    // Repositories for accessing batch, branch, inventory transfer, and inventory deduction data
    private final BatchRepository batchRepository;
    private final ApplicationEventPublisher eventPublisher;

    // Constructor for dependency injection of repositories and event publisher
    public BatchCommandServiceImpl(BatchRepository batchRepository, ApplicationEventPublisher eventPublisher) {
        this.batchRepository = batchRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Handles the creation of a new batch based on the provided CreateBatchCommand. It saves the new batch to the repository and publishes a StockIncreasedEvent.
     *
     * @param command the command containing the necessary information to create a new batch
     * @return an Optional containing the created Batch, or empty if creation failed
     */
    @Override
    public Optional<Batch> handle(CreateBatchCommand command) {
        var batch = Batch.builder()
                .code(command.code())
                .initialStock(command.initialStock())
                .currentStock(command.initialStock())
                .unitPurchaseCost(command.unitPurchaseCost())
                .customSupplyId(command.customSupplyId())
                .receivingBranchId(command.receivingBranchId())
                .accountId(command.accountId())
                .expirationDate(command.expirationDate())
                .manufacturingDate(command.manufacturingDate())
                .entryDate(command.entryDate())
                .build();
        var saved = batchRepository.save(batch);
        eventPublisher.publishEvent(new StockIncreasedEvent(
                saved.getId(), command.receivingBranchId(), command.customSupplyId(), command.initialStock().getValue()));
        return Optional.of(saved);
    }
}
