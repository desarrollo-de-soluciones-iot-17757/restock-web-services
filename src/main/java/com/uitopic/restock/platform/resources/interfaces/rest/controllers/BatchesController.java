package com.uitopic.restock.platform.resources.interfaces.rest.controllers;

import com.uitopic.restock.platform.resources.domain.model.commands.CreateBatchCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.SubtractInventoryCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.TransferInventoryCommand;
import com.uitopic.restock.platform.resources.domain.model.valueobjects.Stock;
import com.uitopic.restock.platform.resources.domain.services.BatchCommandService;
import com.uitopic.restock.platform.resources.domain.services.BatchQueryService;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.*;
import com.uitopic.restock.platform.resources.interfaces.rest.transform.BatchResourceFromEntityAssembler;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.Money;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

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
        var command = new CreateBatchCommand(
                resource.code(),
                new Stock(resource.initialStock(), resource.unitMeasurement()),
                new Money(new BigDecimal(resource.unitPurchaseCostAmount()), resource.unitPurchaseCostCurrency()),
                resource.customSupplyId(),
                resource.receivingBranchId(),
                new AccountId(resource.accountId()),
                resource.manufacturingDate() != null ? Optional.of(LocalDate.parse(resource.manufacturingDate())) : null,
                resource.expirationDate() != null ? Optional.of(LocalDate.parse(resource.expirationDate())) : null,
                resource.entryDate() != null ? Optional.of(LocalDate.parse(resource.entryDate())) : null
        );
        var batch = commandService.handle(command);
        return batch.map(value -> ResponseEntity.status(HttpStatus.CREATED).body(
                BatchResourceFromEntityAssembler
                        .toResourceFromEntity(value)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @Operation(summary = "Get batch by ID")
    @GetMapping("/{batchId}")
    public ResponseEntity<BatchResource> getById(@PathVariable String batchId) {
        return queryService.findById(batchId)
                .map(b -> ResponseEntity.ok(BatchResourceFromEntityAssembler.toResourceFromEntity(b)))
                .orElse(ResponseEntity.notFound().build());
    }

}
