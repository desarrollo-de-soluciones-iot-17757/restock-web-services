package com.uitopic.restock.platform.resources.interfaces.rest.controllers;

import com.uitopic.restock.platform.resources.domain.model.queries.GetBatchesByCustomSupplyIdQuery;
import com.uitopic.restock.platform.resources.domain.services.BatchQueryService;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.BatchResource;
import com.uitopic.restock.platform.resources.interfaces.rest.transform.BatchResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Controller for handling requests related to batches associated with a specific custom supply.
 * This controller provides an endpoint to retrieve batches for a branch, optionally filtered by the custom supply ID.
 *
 * <p>
 *     Endpoints:
 *     <li>GET /api/v1/custom_supply/{customSupplyId}/batches</li>
 * </p>
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/custom_supply/{customSupplyId}/batches", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Custom Supply Batches", description = "Endpoints for retrieving batches by custom supply.")
public class CustomSupplyBatchesController {

    // Service for handling batch queries, injected via constructor
    private final BatchQueryService batchQueryService;

    // Constructor for dependency injection of the BatchQueryService
    public CustomSupplyBatchesController(BatchQueryService batchQueryService) {
        this.batchQueryService = batchQueryService;
    }

    /**
     * Endpoint for fetching batches for a branch, optionally filtered by customSupplyId.
     *
     * @param customSupplyId The ID of the custom supply to filter batches by.
     * @return A ResponseEntity containing a list of BatchResource objects matching the criteria, or an appropriate error response.
     */
    @Operation(summary = "Get batches for a branch (optionally filtered by customSupplyId)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved batches"),
            @ApiResponse(responseCode = "400", description = "Invalid custom supply ID supplied"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
    })
    @GetMapping
    public ResponseEntity<List<BatchResource>> getBatchesByCustomSupplyId(@PathVariable String customSupplyId) {
        if (customSupplyId == null || customSupplyId.isBlank()) {
            log.warn("Custom supply ID is null");
            return ResponseEntity.badRequest().build();
        }

        log.debug("GET /api/v1/custom_supplies/{}/batches", customSupplyId);
        var command = new GetBatchesByCustomSupplyIdQuery(customSupplyId);
        var batches = batchQueryService.handle(command);
        var resources = batches.stream().map(BatchResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }
}
