package com.uitopic.restock.platform.resources.interfaces.rest.controllers;

import com.uitopic.restock.platform.resources.domain.model.commands.CreateBatchCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.SubtractInventoryCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.TransferInventoryCommand;
import com.uitopic.restock.platform.resources.domain.services.BatchCommandService;
import com.uitopic.restock.platform.resources.domain.services.BatchQueryService;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.*;
import com.uitopic.restock.platform.resources.interfaces.rest.transform.BatchResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/batches", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Batches", description = "Batch and inventory operations.")
public class BatchesController {

    private final BatchCommandService commandService;
    private final BatchQueryService queryService;

    public BatchesController(BatchCommandService commandService, BatchQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @Operation(summary = "Create a batch (add stock)")
    @PostMapping
    public ResponseEntity<BatchResource> create(@Valid @RequestBody CreateBatchResource resource) {
        var command = new CreateBatchCommand(resource.accountId(), resource.branchId(),
                resource.customSupplyId(), resource.currentQuantity(), resource.unit(), resource.expirationDate());
        var batch = commandService.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(BatchResourceFromEntityAssembler.toResourceFromEntity(batch));
    }

    @Operation(summary = "Get batch by ID")
    @GetMapping("/{batchId}")
    public ResponseEntity<BatchResource> getById(@PathVariable String batchId) {
        return queryService.findById(batchId)
                .map(b -> ResponseEntity.ok(BatchResourceFromEntityAssembler.toResourceFromEntity(b)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Transfer inventory between branches")
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@Valid @RequestBody CreateInventoryTransferResource resource) {
        var command = new TransferInventoryCommand(resource.fromBranchId(), resource.toBranchId(),
                resource.customSupplyId(), resource.quantity(), resource.unit(), resource.reason());
        var transfer = commandService.handle(command);
        return ResponseEntity.ok(Map.of(
                "transferId", transfer.getId(),
                "status", transfer.getStatus(),
                "completedAt", transfer.getCompletedAt()
        ));
    }

    @Operation(summary = "Subtract inventory stock")
    @PostMapping("/subtract")
    public ResponseEntity<?> subtract(@Valid @RequestBody CreateInventorySubtractResource resource) {
        var command = new SubtractInventoryCommand(resource.branchId(), resource.customSupplyId(),
                resource.quantity(), resource.unit(), resource.reason(), resource.timestamp());
        var deduction = commandService.handle(command);
        return ResponseEntity.ok(Map.of(
                "deductionId", deduction.getId(),
                "remainingStock", deduction.getRemainingStock(),
                "timestamp", deduction.getTimestamp()
        ));
    }

}
