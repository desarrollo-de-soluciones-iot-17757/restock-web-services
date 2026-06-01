package com.uitopic.restock.platform.resources.interfaces.rest.controllers;

import com.uitopic.restock.platform.resources.domain.model.queries.GetBatchesByCustomSupplyIdQuery;
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
 * REST controller for retrieving batches by branch within the resources bounded context.
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/branches/{branchId}/batches", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Branch Batches", description = "Endpoints for retrieving batches by branch.")
public class BranchBatchesController {

    private final BatchQueryService batchQueryService;

    public BranchBatchesController(BatchQueryService batchQueryService) {
        this.batchQueryService = batchQueryService;
    }

    @Operation(summary = "Get batches for a branch (optionally filtered by customSupplyId)")
    @GetMapping
    public ResponseEntity<List<BatchResource>> getBatchesByBranch(
            @PathVariable String branchId,
            @RequestParam(required = false) String customSupplyId) {
        log.debug("GET /api/v1/branches/{}/batches", branchId);
        var batches = batchQueryService.handle(new GetBatchesByCustomSupplyIdQuery(customSupplyId));
        var resources = batches.stream().map(BatchResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }
}
