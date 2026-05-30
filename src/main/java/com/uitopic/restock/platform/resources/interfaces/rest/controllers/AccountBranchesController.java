package com.uitopic.restock.platform.resources.interfaces.rest.controllers;

import com.uitopic.restock.platform.resources.domain.model.commands.CreateBranchCommand;
import com.uitopic.restock.platform.resources.domain.model.queries.GetBranchesByAccountIdQuery;
import com.uitopic.restock.platform.resources.domain.services.BranchCommandService;
import com.uitopic.restock.platform.resources.domain.services.BranchQueryService;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.BranchResource;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.CreateBranchResource;
import com.uitopic.restock.platform.resources.interfaces.rest.transform.BranchResourceFromEntityAssembler;
import com.uitopic.restock.platform.shared.interfaces.rest.transform.SharedValueObjectFromStringAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * AccountBranchesController.java
 * This controller manages branches within the context of a specific account. It provides endpoints to:
 * - Retrieve all branches associated with an account.
 * - Create a new branch under an account.
 * The controller uses BranchCommandService for handling commands related to branch creation and BranchQueryService for fetching branch data.
 * It also includes logging for debugging purposes and is annotated for OpenAPI documentation.
 * The endpoints are designed to be RESTful and return appropriate HTTP status codes based on the outcome of the operations.
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/accounts/{accountId}/branches", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Account Branches", description = "Branch lifecycle management scoped to an account.")
public class AccountBranchesController {

    /** The BranchCommandService is used to handle commands related to branch operations, such as creating a new branch. It encapsulates the business logic for these operations and interacts with the underlying data layer to persist changes. The BranchQueryService is responsible for fetching branch data based on queries, such as retrieving all branches associated with a specific account ID. Both services are injected into the controller through its constructor, allowing the controller to delegate command handling and query processing to these services. */
    private final BranchCommandService branchCommandService;

    /** The BranchQueryService is responsible for handling queries related to branch data retrieval. It provides methods to fetch branch information based on various criteria, such as account ID. In this controller, it is used to retrieve all branches associated with a specific account when the corresponding endpoint is called. The BranchQueryService is injected into the controller through its constructor, allowing the controller to delegate query processing to this service. */
    private final BranchQueryService branchQueryService;

    /** Constructor for AccountBranchesController. This constructor takes two parameters: branchCommandService and branchQueryService. The branchCommandService is used to handle branch creation commands, and the branchQueryService is used to fetch branch data. */
    public AccountBranchesController(BranchCommandService branchCommandService, BranchQueryService branchQueryService) {
        this.branchCommandService = branchCommandService;
        this.branchQueryService = branchQueryService;
    }

    /**
     * Endpoint to retrieve all branches associated with a specific account ID. This method is mapped to a GET request at the path "/api/v1/accounts/{accountId}/branches". It takes the accountId as a path variable, validates it, and uses the BranchQueryService to fetch the branches. The retrieved branches are then transformed into BranchResource objects using the BranchResourceFromEntityAssembler and returned in the response body with an HTTP 200 OK status.
     * @param accountId the unique identifier of the account whose branches are to be retrieved
     * @return a ResponseEntity containing a list of BranchResource objects representing the branches associated with the specified account
     */
    @Operation(summary = "Get all branches for an account")
    @GetMapping
    public ResponseEntity<List<BranchResource>> getBranchesByAccountId(@PathVariable @NotNull String accountId) {
        log.debug("GET /api/v1/accounts/{}/branches", accountId);
        var id = SharedValueObjectFromStringAssembler.toAccountIdFromString(accountId);
        var branches = branchQueryService.handle(new GetBranchesByAccountIdQuery(id));
        var resources = branches.stream().map(BranchResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    /**
     * Endpoint to create a new branch under a specific account. This method is mapped to a POST request at the path "/api/v1/accounts/{accountId}/branches". It takes the accountId as a path variable and a CreateBranchResource object in the request body, which contains the necessary information to create a new branch. The method validates the input, constructs a CreateBranchCommand, and uses the BranchCommandService to handle the command and create the branch. The created branch is then transformed into a BranchResource object using the BranchResourceFromEntityAssembler and returned in the response body with an HTTP 201 Created status.
     * @param accountId the unique identifier of the account under which the new branch is to be created
     * @param resource a CreateBranchResource object containing the necessary information to create a new branch
     * @return a ResponseEntity containing a BranchResource object representing the newly created branch, along with an HTTP 201 Created status
     */
    @Operation(summary = "Create a branch for an account")
    @PostMapping
    public ResponseEntity<BranchResource> createBranch(@PathVariable @NotNull String accountId,
                                                        @Valid @RequestBody CreateBranchResource resource) {
        var command = new CreateBranchCommand(accountId, resource.name(), resource.address(),
                resource.city(), resource.stateOrRegion(), resource.country(), resource.imageUrl(), resource.description());
        var branch = branchCommandService.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BranchResourceFromEntityAssembler.toResourceFromEntity(branch));
    }
}
