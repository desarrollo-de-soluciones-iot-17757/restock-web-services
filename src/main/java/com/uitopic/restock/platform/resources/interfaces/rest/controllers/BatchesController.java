package com.uitopic.restock.platform.resources.interfaces.rest.controllers;

import com.uitopic.restock.platform.resources.domain.model.commands.DeleteBatchCommand;
import com.uitopic.restock.platform.resources.domain.model.queries.GetAllBatchesQuery;
import com.uitopic.restock.platform.resources.domain.model.queries.GetBatchByIdQuery;
import com.uitopic.restock.platform.resources.domain.model.queries.GetBatchesByAccountIdQuery;
import com.uitopic.restock.platform.resources.domain.model.queries.GetBatchesByBranchIdQuery;
import com.uitopic.restock.platform.resources.domain.model.queries.GetBatchesByCustomSupplyIdQuery;
import com.uitopic.restock.platform.resources.domain.services.BatchCommandService;
import com.uitopic.restock.platform.resources.domain.services.BatchQueryService;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.BatchResource;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.CreateBatchResource;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.TransferBatchStockResource;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.UpdateBatchResource;
import com.uitopic.restock.platform.resources.interfaces.rest.transform.BatchResourceFromEntityAssembler;
import com.uitopic.restock.platform.resources.interfaces.rest.transform.CreateBatchCommandFromResourceAssembler;
import com.uitopic.restock.platform.resources.interfaces.rest.transform.TransferBatchStockCommandFromResourceAssembler;
import com.uitopic.restock.platform.resources.interfaces.rest.transform.UpdateBatchCommandFromResourceAssembler;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller for batch management.
 *
 * Provides batch queries, creation, update, deletion and stock transfer between
 * branches. Optional query parameters are used for filtering without breaking
 * the base endpoint naming.
 */
@RestController
@RequestMapping(value = "/api/v1/batches", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Batches", description = "Batch management and query endpoints.")
public class BatchesController {

    private final BatchCommandService batchCommandService;
    private final BatchQueryService batchQueryService;

    /**
     * Creates a BatchesController with the required command and query services.
     *
     * @param batchCommandService service used to execute batch write operations
     * @param batchQueryService service used to execute batch read operations
     */
    public BatchesController(
            BatchCommandService batchCommandService,
            BatchQueryService batchQueryService
    ) {
        this.batchCommandService = batchCommandService;
        this.batchQueryService = batchQueryService;
    }

    /**
     * Gets batches using optional query parameters.
     *
     * If no query parameter is provided, all batches are returned.
     * Only one filter is allowed at a time to keep the query behavior explicit.
     *
     * Examples:
     * GET /api/v1/batches
     * GET /api/v1/batches?accountId=acc-123
     * GET /api/v1/batches?branchId=branch-123
     * GET /api/v1/batches?customSupplyId=cs-123
     *
     * @param accountId optional account identifier
     * @param branchId optional branch identifier
     * @param customSupplyId optional custom supply identifier
     * @return list of batch resources
     */
    @Operation(summary = "Get batches with optional filters")
    @GetMapping
    public ResponseEntity<List<BatchResource>> getAll(
            @RequestParam(required = false) String accountId,
            @RequestParam(required = false) String branchId,
            @RequestParam(required = false) String customSupplyId
    ) {
        validateOnlyOneFilter(accountId, branchId, customSupplyId);

        var batches = hasValue(accountId)
                ? batchQueryService.handle(new GetBatchesByAccountIdQuery(new AccountId(accountId)))
                : hasValue(branchId)
                  ? batchQueryService.handle(new GetBatchesByBranchIdQuery(branchId))
                  : hasValue(customSupplyId)
                    ? batchQueryService.handle(new GetBatchesByCustomSupplyIdQuery(customSupplyId))
                    : batchQueryService.handle(new GetAllBatchesQuery());

        var resources = batches.stream()
                .map(BatchResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    /**
     * Creates a new batch.
     *
     * The account identifier is received as a query parameter. The unit of
     * measurement is obtained from the related custom supply, not from the body.
     *
     * Example:
     * POST /api/v1/batches?accountId=acc-123
     *
     * @param accountId account identifier
     * @param resource request body with batch data
     * @return created batch resource
     */
    @Operation(summary = "Create batch")
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<BatchResource> create(
            @RequestParam String accountId,
            @Valid @RequestBody CreateBatchResource resource
    ) {
        var command = CreateBatchCommandFromResourceAssembler.toCommandFromResource(accountId, resource);
        var batch = batchCommandService.handle(command);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BatchResourceFromEntityAssembler.toResourceFromEntity(batch));
    }

    /**
     * Gets a batch by its identifier.
     *
     * If the batch is not found, a ResponseStatusException is thrown so the
     * GlobalExceptionHandler can return a structured error response.
     *
     * @param batchId batch identifier
     * @return batch resource
     */
    @Operation(summary = "Get batch by ID")
    @GetMapping("/{batchId}")
    public ResponseEntity<BatchResource> getById(@PathVariable String batchId) {
        var batch = batchQueryService.handle(new GetBatchByIdQuery(batchId))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Batch not found: " + batchId
                ));

        return ResponseEntity.ok(BatchResourceFromEntityAssembler.toResourceFromEntity(batch));
    }

    /**
     * Updates an existing batch.
     *
     * Entry date is not updated because it represents when the batch was
     * registered.
     *
     * @param batchId batch identifier
     * @param resource request body with updated batch data
     * @return updated batch resource
     */
    @Operation(summary = "Update batch")
    @PatchMapping(value = "/{batchId}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<BatchResource> update(
            @PathVariable String batchId,
            @Valid @RequestBody UpdateBatchResource resource
    ) {
        var command = UpdateBatchCommandFromResourceAssembler.toCommandFromResource(batchId, resource);

        var batch = batchCommandService.handle(command)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Batch not found: " + batchId
                ));

        return ResponseEntity.ok(BatchResourceFromEntityAssembler.toResourceFromEntity(batch));
    }

    /**
     * Deletes a batch by its identifier.
     *
     * @param batchId batch identifier
     * @return deletion confirmation
     */
    @Operation(summary = "Delete batch")
    @DeleteMapping("/{batchId}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable String batchId) {
        batchCommandService.handle(new DeleteBatchCommand(batchId));

        return ResponseEntity.ok(Map.of(
                "id", batchId,
                "deletedAt", Instant.now().toString()
        ));
    }

    /**
     * Transfers stock from a batch to another branch.
     *
     * This is a POST because it represents an action that modifies more than
     * one batch and is not idempotent.
     *
     * @param batchId source batch identifier
     * @param resource request body with target branch and quantity
     * @return affected batches
     */
    @Operation(summary = "Transfer batch stock to another branch")
    @PostMapping(value = "/{batchId}/transfer", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BatchResource>> transfer(
            @PathVariable String batchId,
            @Valid @RequestBody TransferBatchStockResource resource
    ) {
        var command = TransferBatchStockCommandFromResourceAssembler.toCommandFromResource(
                batchId,
                resource
        );

        var batches = batchCommandService.handle(command);

        var response = batches.stream()
                .map(BatchResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(response);
    }

    private void validateOnlyOneFilter(String accountId, String branchId, String customSupplyId) {
        int filters = 0;

        if (hasValue(accountId)) filters++;
        if (hasValue(branchId)) filters++;
        if (hasValue(customSupplyId)) filters++;

        if (filters > 1) {
            throw new IllegalArgumentException("Only one filter can be used at a time");
        }
    }

    private boolean hasValue(String value) {
        return value != null && !value.isBlank();
    }
}