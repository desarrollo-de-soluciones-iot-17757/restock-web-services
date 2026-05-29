package com.uitopic.restock.platform.resources.interfaces.rest.controllers;

import com.uitopic.restock.platform.resources.domain.model.queries.GetCustomSuppliesByAccountIdQuery;
import com.uitopic.restock.platform.resources.domain.services.CustomSupplyQueryService;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.CustomSupplyWrapper;
import com.uitopic.restock.platform.resources.interfaces.rest.transform.CustomSupplyWrapperFromEntitiesAssembler;
import com.uitopic.restock.platform.shared.interfaces.rest.transform.SharedValueObjectFromStringAssembler;
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
 * Controller for handling REST API requests related to custom supplies associated with accounts. Provides endpoints to retrieve custom supplies for a specific account.
 * This controller is part of the REST API layer and interacts with the CustomSupplyQueryService to fetch data and return it in a structured format.
 *
 * <p>
 *    Endpoints:
 *    <li> GET /api/v1/accounts/{accountId}/custom-supplies: Retrieves a list of custom supplies associated with the specified account ID.
 * </p>
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/accounts/{accountId}/custom-supplies", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Account Custom Supplies", description = "Endpoints for managing custom supplies associated with accounts.")
public class AccountCustomSuppliesController {

    // Service for handling queries related to custom supplies
    private final CustomSupplyQueryService customSupplyQueryService;

    // Constructor injection of the CustomSupplyQueryService
    public AccountCustomSuppliesController(CustomSupplyQueryService customSupplyQueryService) {
        this.customSupplyQueryService = customSupplyQueryService;
    }

    /**
     * Endpoint to retrieve custom supplies for a specific account by its ID.
     *
     * @param accountId The ID of the account for which to retrieve custom supplies.
     * @return A ResponseEntity containing a CustomSupplyWrapper with the list of custom supplies for the account, or an appropriate error response.
     */
    @Operation(
            summary = "Get Custom Supplies by Account ID",
            description = "Retrieves a list of custom supplies associated with the specified account ID.",
            operationId = "getCustomSuppliesByAccountId"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved custom supplies for the account."),
            @ApiResponse(responseCode = "401", description = "Unauthorized access to the account's custom supplies."),
            @ApiResponse(responseCode = "500", description = "Internal server error while retrieving custom supplies."),
    })
    @GetMapping
    public ResponseEntity<CustomSupplyWrapper> getCustomSuppliesByAccountId(@PathVariable @NotNull String accountId) {
        log.debug("GET /api/v1/accounts/{}/custom-supplies", accountId);
        var id = SharedValueObjectFromStringAssembler.toAccountIdFromString(accountId);
        var getCustomSuppliesByAccountIdQuery = new GetCustomSuppliesByAccountIdQuery(id);
        var customSupplies = customSupplyQueryService.handle(getCustomSuppliesByAccountIdQuery);
        log.debug("Found {} custom supplies for account {}", customSupplies.size(), accountId);
        var resource = CustomSupplyWrapperFromEntitiesAssembler.toWrapperFromEntities(id, customSupplies);
        return ResponseEntity.ok(resource);
    }
}
