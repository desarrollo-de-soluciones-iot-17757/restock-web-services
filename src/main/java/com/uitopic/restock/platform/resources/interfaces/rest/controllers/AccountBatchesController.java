package com.uitopic.restock.platform.resources.interfaces.rest.controllers;

import com.uitopic.restock.platform.resources.domain.model.queries.GetBatchesByBranchIdQuery;
import com.uitopic.restock.platform.resources.domain.services.BatchQueryService;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.BatchResource;
import com.uitopic.restock.platform.resources.interfaces.rest.transform.BatchResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller for retrieving batches by account within the resources bounded context.
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/accounts/{accountId}/batches", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Account Batches", description = "Endpoints for retrieving batches by account.")
public class AccountBatchesController {

    private final BatchQueryService batchQueryService;

    public AccountBatchesController(BatchQueryService batchQueryService) {
        this.batchQueryService = batchQueryService;
    }

    @Operation(summary = "Get all batches for an account (optionally filtered by customSupplyId)")
    @GetMapping
    public ResponseEntity<List<BatchResource>> getBatchesByAccountId(
            @PathVariable String accountId,
            @RequestParam(required = false) String customSupplyId) {
        log.debug("GET /api/v1/accounts/{}/batches", accountId);
        var batches = batchQueryService.handle(new GetBatchesByBranchIdQuery(null, customSupplyId));
        var resources = batches.stream().map(BatchResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }
}
