package com.uitopic.restock.platform.resources.application.internal.commandservices;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Batch;
import com.uitopic.restock.platform.resources.domain.model.aggregates.Branch;
import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.resources.domain.model.commands.CreateBatchCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.SubtractInventoryCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.TransferInventoryCommand;
import com.uitopic.restock.platform.resources.domain.model.entities.Inventory;
import com.uitopic.restock.platform.resources.domain.model.entities.InventoryDeduction;
import com.uitopic.restock.platform.resources.domain.model.entities.InventoryTransfer;
import com.uitopic.restock.platform.resources.domain.model.events.StockIncreasedEvent;
import com.uitopic.restock.platform.resources.domain.model.events.StockSubtractedEvent;
import com.uitopic.restock.platform.resources.domain.model.events.StockTransferredEvent;
import com.uitopic.restock.platform.resources.domain.model.valueobjects.Stock;
import com.uitopic.restock.platform.resources.domain.repositories.BatchRepository;
import com.uitopic.restock.platform.resources.domain.repositories.BranchRepository;
import com.uitopic.restock.platform.resources.domain.repositories.CustomSupplyRepository;
import com.uitopic.restock.platform.resources.domain.repositories.InventoryDeductionRepository;
import com.uitopic.restock.platform.resources.domain.repositories.InventoryTransferRepository;
import com.uitopic.restock.platform.resources.domain.services.BatchCommandService;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BatchCommandServiceImpl implements BatchCommandService {

    private final BatchRepository batchRepository;
    private final BranchRepository branchRepository;
    private final CustomSupplyRepository customSupplyRepository;
    private final InventoryTransferRepository transferRepository;
    private final InventoryDeductionRepository deductionRepository;
    private final ApplicationEventPublisher eventPublisher;

    public BatchCommandServiceImpl(BatchRepository batchRepository,
                                   BranchRepository branchRepository,
                                   CustomSupplyRepository customSupplyRepository,
                                   InventoryTransferRepository transferRepository,
                                   InventoryDeductionRepository deductionRepository,
                                   ApplicationEventPublisher eventPublisher) {
        this.batchRepository = batchRepository;
        this.branchRepository = branchRepository;
        this.customSupplyRepository = customSupplyRepository;
        this.transferRepository = transferRepository;
        this.deductionRepository = deductionRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Batch handle(CreateBatchCommand command) {
        log.info("Creating batch for branch ID: {}, customSupply ID: {}, quantity: {}",
                command.branchId(), command.customSupplyId(), command.currentQuantity());
        Branch branch = branchRepository.findById(command.branchId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Branch not found"));
        CustomSupply customSupply = customSupplyRepository.findById(command.customSupplyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CustomSupply not found"));

        LocalDate expDate = (command.expirationDate() != null && !command.expirationDate().isBlank())
                ? LocalDate.parse(command.expirationDate()) : null;

        Batch batch = Batch.builder()
                .accountId(new AccountId(command.accountId()))
                .receivingBranch(branch)
                .customSupply(customSupply)
                .initialStock(new Stock((int) command.currentQuantity()))
                .currentStock(new Stock((int) command.currentQuantity()))
                .expirationDate(expDate)
                .entryDate(LocalDate.now())
                .build();
        Batch saved = batchRepository.save(batch);
        log.info("Batch created with ID: {}", saved.getId());
        eventPublisher.publishEvent(new StockIncreasedEvent(
                saved.getId(), command.branchId(), command.customSupplyId(), command.currentQuantity()));
        return saved;
    }

    @Override
    public InventoryTransfer handle(TransferInventoryCommand command) {
        log.info("Transferring stock — from: {}, to: {}, supplyId: {}, qty: {}",
                command.fromBranchId(), command.toBranchId(), command.customSupplyId(), command.quantity());
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
            log.warn("Insufficient stock for transfer — available: {}, requested: {}", available, command.quantity());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Insufficient stock: available=" + available + ", requested=" + command.quantity());
        }
        deductFifo(batches, command.quantity());

        CustomSupply customSupply = customSupplyRepository.findById(command.customSupplyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CustomSupply not found"));

        batchRepository.save(Batch.builder()
                .accountId(fromBranch.getAccountId())
                .receivingBranch(toBranch)
                .customSupply(customSupply)
                .initialStock(new Stock((int) command.quantity()))
                .currentStock(new Stock((int) command.quantity()))
                .entryDate(LocalDate.now())
                .build());

        Inventory originInventory = Inventory.builder()
                .branch(fromBranch)
                .batch(batches.isEmpty() ? null : batches.get(0))
                .currentStock(new Stock((int) (available - command.quantity())))
                .build();

        InventoryTransfer transfer = InventoryTransfer.builder()
                .originInventory(originInventory)
                .destinationBranch(toBranch)
                .quantityTransferred(new Stock((int) command.quantity()))
                .transferDate(LocalDate.now())
                .build();
        InventoryTransfer saved = transferRepository.save(transfer);
        log.info("Transfer completed — ID: {}", saved.getId());
        eventPublisher.publishEvent(new StockTransferredEvent(saved.getId(),
                command.fromBranchId(), command.toBranchId(), command.customSupplyId(), command.quantity()));
        return saved;
    }

    @Override
    public InventoryDeduction handle(SubtractInventoryCommand command) {
        log.info("Subtracting stock — branchId: {}, supplyId: {}, qty: {}",
                command.branchId(), command.customSupplyId(), command.quantity());
        Branch branch = branchRepository.findById(command.branchId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Branch not found"));
        List<Batch> batches = activeBatches(command.branchId(), command.customSupplyId());
        double available = totalQuantity(batches);
        if (available < command.quantity()) {
            log.warn("Insufficient stock for deduction — available: {}, requested: {}", available, command.quantity());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Insufficient stock: available=" + available + ", requested=" + command.quantity());
        }
        deductFifo(batches, command.quantity());
        double remainingStock = available - command.quantity();

        Batch batch = batches.isEmpty() ? null : batches.get(0);
        Inventory inventory = Inventory.builder()
                .branch(branch)
                .batch(batch)
                .currentStock(new Stock((int) remainingStock))
                .build();

        LocalDate dedDate;
        try {
            dedDate = (command.timestamp() != null && !command.timestamp().isBlank())
                    ? LocalDate.parse(command.timestamp().substring(0, 10))
                    : LocalDate.now();
        } catch (Exception e) {
            dedDate = LocalDate.now();
        }

        InventoryDeduction deduction = InventoryDeduction.builder()
                .inventory(inventory)
                .quantityDeducted(new Stock((int) command.quantity()))
                .deductionDate(dedDate)
                .build();
        InventoryDeduction saved = deductionRepository.save(deduction);
        log.info("Deduction recorded — ID: {}, remaining stock: {}", saved.getId(), remainingStock);
        eventPublisher.publishEvent(new StockSubtractedEvent(
                saved.getId(), command.branchId(), command.customSupplyId(), command.quantity(), remainingStock));
        return saved;
    }

    @Override
    public double subtractStock(String branchId, String customSupplyId, double quantity) {
        log.info("Subtracting stock (ACL) — branchId: {}, supplyId: {}, qty: {}", branchId, customSupplyId, quantity);
        List<Batch> batches = activeBatches(branchId, customSupplyId);
        double available = totalQuantity(batches);
        if (available < quantity) {
            log.warn("Insufficient stock — available: {}, required: {}", available, quantity);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Insufficient stock for supply " + customSupplyId + ": available=" + available + ", required=" + quantity);
        }
        deductFifo(batches, quantity);
        log.info("Stock subtracted — remaining: {}", available - quantity);
        return available - quantity;
    }

    @Override
    public void addStockBack(String branchId, String customSupplyId, double quantity, String unit) {
        log.info("Adding stock back — branchId: {}, supplyId: {}, qty: {}", branchId, customSupplyId, quantity);
        Branch branch = branchRepository.findById(branchId).orElse(null);
        AccountId accountId = branch != null ? branch.getAccountId() : null;
        CustomSupply customSupply = customSupplyRepository.findById(customSupplyId).orElse(null);

        batchRepository.save(Batch.builder()
                .accountId(accountId)
                .receivingBranch(branch)
                .customSupply(customSupply)
                .initialStock(new Stock((int) quantity))
                .currentStock(new Stock((int) quantity))
                .entryDate(LocalDate.now())
                .build());
        log.info("Stock added back successfully for supplyId: {}", customSupplyId);
    }

    @Override
    public void adjustStock(String branchId, String customSupplyId, double adjustedQuantity, String unit) {
        log.info("Adjusting stock — branchId: {}, supplyId: {}, adjustedQty: {}", branchId, customSupplyId, adjustedQuantity);
        batchRepository.findByBranchIdAndCustomSupplyId(branchId, customSupplyId)
                .stream().filter(b -> b.getCurrentStock() != null && b.getCurrentStock().stock() > 0)
                .forEach(b -> {
                    b.setCurrentStock(new Stock(0));
                    batchRepository.save(b);
                });
        if (adjustedQuantity > 0) {
            Branch branch = branchRepository.findById(branchId).orElse(null);
            AccountId accountId = branch != null ? branch.getAccountId() : null;
            CustomSupply customSupply = customSupplyRepository.findById(customSupplyId).orElse(null);

            batchRepository.save(Batch.builder()
                    .accountId(accountId)
                    .receivingBranch(branch)
                    .customSupply(customSupply)
                    .initialStock(new Stock((int) adjustedQuantity))
                    .currentStock(new Stock((int) adjustedQuantity))
                    .entryDate(LocalDate.now())
                    .build());
        }
        log.info("Stock adjusted successfully for supplyId: {}", customSupplyId);
    }

    private List<Batch> activeBatches(String branchId, String customSupplyId) {
        return batchRepository.findByBranchIdAndCustomSupplyId(branchId, customSupplyId)
                .stream().filter(b -> b.getCurrentStock() != null && b.getCurrentStock().stock() > 0)
                .sorted(Comparator.comparing(Batch::getCreatedAt))
                .collect(Collectors.toList());
    }

    private double totalQuantity(List<Batch> batches) {
        return batches.stream().mapToDouble(b -> b.getCurrentStock() != null ? b.getCurrentStock().stock() : 0.0).sum();
    }

    private void deductFifo(List<Batch> batches, double toDeduct) {
        double remaining = toDeduct;
        for (Batch batch : batches) {
            if (remaining <= 0) break;
            double currentStockVal = batch.getCurrentStock() != null ? batch.getCurrentStock().stock() : 0.0;
            double take = Math.min(currentStockVal, remaining);
            batch.setCurrentStock(new Stock((int) (currentStockVal - take)));
            batchRepository.save(batch);
            remaining -= take;
        }
    }
}
