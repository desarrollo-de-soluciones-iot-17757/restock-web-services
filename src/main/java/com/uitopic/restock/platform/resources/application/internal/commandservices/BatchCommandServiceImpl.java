package com.uitopic.restock.platform.resources.application.internal.commandservices;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Batch;
import com.uitopic.restock.platform.resources.domain.model.aggregates.Branch;
import com.uitopic.restock.platform.resources.domain.model.commands.CreateBatchCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.SubtractInventoryCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.TransferInventoryCommand;
import com.uitopic.restock.platform.resources.domain.model.entities.InventoryDeduction;
import com.uitopic.restock.platform.resources.domain.model.entities.InventoryTransfer;
import com.uitopic.restock.platform.resources.domain.model.events.StockIncreasedEvent;
import com.uitopic.restock.platform.resources.domain.model.events.StockSubtractedEvent;
import com.uitopic.restock.platform.resources.domain.model.events.StockTransferredEvent;
import com.uitopic.restock.platform.resources.domain.model.valueobjects.Stock;
import com.uitopic.restock.platform.resources.domain.repositories.BatchRepository;
import com.uitopic.restock.platform.resources.domain.repositories.BranchRepository;
import com.uitopic.restock.platform.resources.domain.repositories.InventoryDeductionRepository;
import com.uitopic.restock.platform.resources.domain.repositories.InventoryTransferRepository;
import com.uitopic.restock.platform.resources.domain.services.BatchCommandService;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.UnitMeasurement;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BatchCommandServiceImpl implements BatchCommandService {

    private final BatchRepository batchRepository;
    private final BranchRepository branchRepository;
    private final InventoryTransferRepository transferRepository;
    private final InventoryDeductionRepository deductionRepository;
    private final ApplicationEventPublisher eventPublisher;

    public BatchCommandServiceImpl(BatchRepository batchRepository,
                                   BranchRepository branchRepository,
                                   InventoryTransferRepository transferRepository,
                                   InventoryDeductionRepository deductionRepository,
                                   ApplicationEventPublisher eventPublisher) {
        this.batchRepository = batchRepository;
        this.branchRepository = branchRepository;
        this.transferRepository = transferRepository;
        this.deductionRepository = deductionRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Optional<Batch> handle(CreateBatchCommand command) {
        Batch batch = Batch.builder()
                .code(command.code())
                .initialStock(command.initialStock())
                .currentStock(command.initialStock())
                .unitPurchaseCost(command.unitPurchaseCost())
                .customSupplyId(command.customSupplyId())
                .receivingBranchId(command.receivingBranchId())
                .accountId(command.accountId())
                .expirationDate(Optional.ofNullable(command.expirationDate().orElse(null)))
                .manufacturingDate(Optional.ofNullable(command.manufacturingDate().orElse(null)))
                .entryDate(Optional.ofNullable(command.entryDate().orElse(null)))
                .build();
        Batch saved = batchRepository.save(batch);
        eventPublisher.publishEvent(new StockIncreasedEvent(
                saved.getId(), command.receivingBranchId(), command.customSupplyId(), command.initialStock().getValue()));
        return Optional.of(saved);
    }

    @Override
    public Optional<InventoryTransfer> handle(TransferInventoryCommand command) {
        Branch fromBranch = branchRepository.findById(command.fromBranchId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "From branch not found"));
        Branch toBranch = branchRepository.findById(command.toBranchId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "To branch not found"));
        if (!fromBranch.getAccountId().getAccountId().equals(toBranch.getAccountId().getAccountId())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Branches belong to different accounts");
        }

        List<Batch> batches = activeBatches(command.fromBranchId(), command.customSupplyId());
        double available = totalQuantity(batches);
        if (available < command.quantity()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Insufficient stock: available=" + available + ", requested=" + command.quantity());
        }
        deductFifo(batches, command.quantity());

        UnitMeasurement unit = new UnitMeasurement(command.unit());
        batchRepository.save(Batch.builder()
                .accountId(fromBranch.getAccountId())
                .receivingBranchId(command.toBranchId())
                .customSupplyId(command.customSupplyId())
                .currentStock(new Stock(command.quantity()))
                .build());

        InventoryTransfer transfer = InventoryTransfer.builder()
                .fromBranchId(command.fromBranchId())
                .toBranchId(command.toBranchId())
                .supplyId(command.customSupplyId())
                .quantity(command.quantity())
                .unit(unit)
                .reason(command.reason())
                .status("completed")
                .completedAt(Instant.now().toString())
                .build();
        InventoryTransfer saved = transferRepository.save(transfer);
        eventPublisher.publishEvent(new StockTransferredEvent(saved.getId(),
                command.fromBranchId(), command.toBranchId(), command.customSupplyId(), command.quantity()));
        return Optional.of(saved);
    }

    @Override
    public Optional<InventoryDeduction> handle(SubtractInventoryCommand command) {
        List<Batch> batches = activeBatches(command.branchId(), command.customSupplyId());
        double available = totalQuantity(batches);
        if (available < command.quantity()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Insufficient stock: available=" + available + ", requested=" + command.quantity());
        }
        deductFifo(batches, command.quantity());
        double remainingStock = available - command.quantity();

        String ts = (command.timestamp() != null && !command.timestamp().isBlank())
                ? command.timestamp() : Instant.now().toString();
        InventoryDeduction deduction = InventoryDeduction.builder()
                .branchId(command.branchId())
                .supplyId(command.customSupplyId())
                .quantity(command.quantity())
                .unit(new UnitMeasurement(command.unit()))
                .reason(command.reason())
                .timestamp(ts)
                .remainingStock(remainingStock)
                .build();
        InventoryDeduction saved = deductionRepository.save(deduction);
        eventPublisher.publishEvent(new StockSubtractedEvent(
                saved.getId(), command.branchId(), command.customSupplyId(), command.quantity(), remainingStock));
        return Optional.of(saved);
    }

    @Override
    public double subtractStock(String branchId, String customSupplyId, Integer quantity) {
        List<Batch> batches = activeBatches(branchId, customSupplyId);
        double available = totalQuantity(batches);
        if (available < quantity) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Insufficient stock for supply " + customSupplyId + ": available=" + available + ", required=" + quantity);
        }
        deductFifo(batches, quantity);
        return available - quantity;
    }

    @Override
    public void addStockBack(String branchId, String customSupplyId, Integer quantity, String unit) {
        AccountId accountId = branchRepository.findById(branchId).map(Branch::getAccountId).orElse(null);
        batchRepository.save(Batch.builder()
                .accountId(accountId)
                .receivingBranchId(branchId)
                .customSupplyId(customSupplyId)
                .currentStock(new Stock(quantity))
                .build());
    }

    @Override
    public void adjustStock(String branchId, String customSupplyId, Integer adjustedQuantity, String unit) {
        batchRepository.findByBranchIdAndCustomSupplyId(branchId, customSupplyId)
                .stream().filter(b -> b.getCurrentStock().getValue() > 0)
                .forEach(b -> {
                    b.setCurrentStock(new Stock(0));
                    batchRepository.save(b);
                });
        if (adjustedQuantity > 0) {
            AccountId accountId = branchRepository.findById(branchId).map(Branch::getAccountId).orElse(null);
            batchRepository.save(Batch.builder()
                    .accountId(accountId)
                    .receivingBranchId(branchId)
                    .customSupplyId(customSupplyId)
                    .currentStock(new Stock(adjustedQuantity))
                    .build());
        }
    }

    private List<Batch> activeBatches(String branchId, String customSupplyId) {
        return batchRepository.findByBranchIdAndCustomSupplyId(branchId, customSupplyId)
                .stream().filter(b -> b.getCurrentStock().getValue() > 0)
                .sorted(Comparator.comparing(Batch::getCreatedAt))
                .collect(Collectors.toList());
    }

    private double totalQuantity(List<Batch> batches) {
        return batches.stream().mapToInt(b -> b.getCurrentStock().getValue()).sum();
    }

    private void deductFifo(List<Batch> batches, Integer toDeduct) {
        double remaining = toDeduct;
        for (Batch batch : batches) {
            if (remaining <= 0) break;
            double take = Math.min(batch.getCurrentStock().getValue(), remaining);
            Integer valueToTake = (int) Math.round(take);
            batch.setCurrentStock(new Stock(batch.getCurrentStock().getValue() - valueToTake));
            batchRepository.save(batch);
            remaining -= take;
        }
    }
}
