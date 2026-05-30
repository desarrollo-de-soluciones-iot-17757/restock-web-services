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

/**
 * BatchesController.java
 * This controller manages batch and inventory operations. It provides endpoints to:
 * - Create a new batch (add stock).
 * - Get batch details by ID.
 * - Transfer inventory between branches.
 * - Subtract inventory stock.
 * The controller uses BatchCommandService for handling commands related to batch and inventory operations and BatchQueryService for fetching batch data. It includes logging for debugging purposes and is annotated for OpenAPI documentation. The endpoints are designed to be RESTful and return appropriate HTTP status codes based on the outcome of the operations.
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/batches", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Batches", description = "Batch and inventory operations.")
public class BatchesController {

    /** The BatchCommandService is used to handle commands related to batch and inventory operations, such as creating a new batch, transferring inventory, and subtracting inventory stock. It encapsulates the business logic for these operations and interacts with the underlying data layer to persist changes. The BatchQueryService is responsible for fetching batch data based on queries, such as retrieving batch details by ID. Both services are injected into the controller through its constructor, allowing the controller to delegate command handling and query processing to these services. */
    private final BatchCommandService commandService;

    /** The BatchQueryService is responsible for handling queries related to batch data retrieval. It provides methods to fetch batch information based on various criteria, such as batch ID. In this controller, it is used to retrieve batch details when the corresponding endpoint is called. The BatchQueryService is injected into the controller through its constructor, allowing the controller to delegate query processing to this service. */
    private final BatchQueryService queryService;

    /** Constructor for BatchesController. This constructor takes two parameters: commandService and queryService. The commandService is used to handle batch and inventory commands, and the queryService is used to fetch batch data. */
    public BatchesController(BatchCommandService commandService, BatchQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    /**
     * Endpoint to create a new batch (add stock). This method is mapped to a POST request at the path "/api/v1/batches". It takes a CreateBatchResource object as the request body, validates it, and uses the BatchCommandService to handle the creation of the batch. The created batch is then transformed into a BatchResource object using the BatchResourceFromEntityAssembler and returned in the response body with an HTTP 201 Created status.
     * @param resource the CreateBatchResource object containing the necessary information to create a new batch
     * @return a ResponseEntity containing a BatchResource object representing the created batch, with an HTTP 201 Created status
     */
    @Operation(summary = "Create a batch (add stock)")
    @PostMapping
    public ResponseEntity<BatchResource> create(@Valid @RequestBody CreateBatchResource resource) {
        var command = new CreateBatchCommand(resource.accountId(), resource.branchId(),
                resource.customSupplyId(), resource.currentQuantity(), resource.unit(), resource.expirationDate());
        var batch = commandService.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(BatchResourceFromEntityAssembler.toResourceFromEntity(batch));
    }

    /**
     * Endpoint to get batch details by ID. This method is mapped to a GET request at the path "/api/v1/batches/{batchId}". It takes the batchId as a path variable, validates it, and uses the BatchQueryService to fetch the batch details. If a batch with the specified ID is found, it is transformed into a BatchResource object using the BatchResourceFromEntityAssembler and returned in the response body with an HTTP 200 OK status. If no batch with the specified ID is found, an HTTP 404 Not Found status is returned.
     * @param batchId the unique identifier of the batch to retrieve
     * @return a ResponseEntity containing a BatchResource object representing the batch details if found, or an HTTP 404 Not Found status if no batch with the specified ID exists
     */
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
