package com.uitopic.restock.platform.resources.interfaces.rest.controllers;

import com.uitopic.restock.platform.resources.domain.model.queries.GetInventoriesByBranchIdQuery;
import com.uitopic.restock.platform.resources.domain.services.InventoryQueryService;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.InventoryWrapper;
import com.uitopic.restock.platform.resources.interfaces.rest.transform.InventoryWrapperFromEntitiesAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Controller for handling REST API requests related to inventories associated with branches. Provides endpoints to retrieve inventories for a specific branch.
 * This controller is part of the REST API layer and interacts with the InventoryQueryService to fetch data and return it in a structured format.
 *
 * <p>
 *  *    Endpoints:
 *  *    <li> GET /api/v1/branches/{branchId}/inventories: Retrieves a list of inventories associated with the specified branch ID.
 *  * </p>
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/branches/{branchId}/inventories", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Branch Inventories", description = "Endpoints for managing inventories associated with branches.")
public class BranchInventoriesController {

    // Service for handling queries related to inventories
    private final InventoryQueryService inventoryQueryService;

    // Constructor injection of the InventoryQueryService
    public BranchInventoriesController(InventoryQueryService inventoryQueryService) {
        this.inventoryQueryService = inventoryQueryService;
    }

    /**
     * Endpoint to retrieve inventories for a specific branch by its ID.
     *
     * @param branchId The ID of the branch for which to retrieve inventories.
     * @return A ResponseEntity containing an InventoryWrapper with the list of inventories for the branch, or an appropriate error response.
     */
    @Operation(
            summary = "Get Inventories by Branch ID",
            description = "Retrieves a list of inventories associated with the specified branch ID.",
            operationId = "getInventoriesByBranchId"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved inventories for the branch."),
            @ApiResponse(responseCode = "401", description = "Unauthorized access to the branch's inventories."),
            @ApiResponse(responseCode = "500", description = "Internal server error while retrieving inventories."),
    })
    @GetMapping
    public ResponseEntity<InventoryWrapper> getInventoriesByBranchId(@PathVariable @NotNull String branchId) {
        log.debug("GET /api/v1/branches/{}/inventories", branchId);
        var getInventoriesByBranchIdQuery = new GetInventoriesByBranchIdQuery(branchId);
        var resources = inventoryQueryService.handle(getInventoriesByBranchIdQuery);
        var response = InventoryWrapperFromEntitiesAssembler.toWrapperFromEntities(branchId, resources.getLeft(), resources.getMiddle(), resources.getRight());
        return ResponseEntity.ok(response);
    }
}
