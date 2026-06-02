package com.uitopic.restock.platform.resources.interfaces.rest.controllers;

import com.uitopic.restock.platform.resources.domain.model.commands.DeleteBranchCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.UpdateBranchStatusCommand;
import com.uitopic.restock.platform.resources.domain.model.queries.GetAllBranchesQuery;
import com.uitopic.restock.platform.resources.domain.model.queries.GetBranchByIdQuery;
import com.uitopic.restock.platform.resources.domain.model.queries.GetBranchesByAccountIdQuery;
import com.uitopic.restock.platform.resources.domain.services.BranchCommandService;
import com.uitopic.restock.platform.resources.domain.services.BranchQueryService;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.BranchResource;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.CreateBranchResource;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.UpdateBranchResource;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.UpdateBranchStatusResource;
import com.uitopic.restock.platform.resources.interfaces.rest.transform.BranchResourceFromEntityAssembler;
import com.uitopic.restock.platform.resources.interfaces.rest.transform.CreateBranchCommandFromResourceAssembler;
import com.uitopic.restock.platform.resources.interfaces.rest.transform.UpdateBranchCommandFromResourceAssembler;
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
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

/**
 * REST controller for branch management.
 *
 * Endpoints that receive images use multipart/form-data.
 */
@RestController
@RequestMapping(value = "/api/v1/branches", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Branches", description = "Branch management and query endpoints.")
public class BranchesController {

    private final BranchCommandService branchCommandService;
    private final BranchQueryService branchQueryService;

    /**
     * Creates a BranchesController with the required command and query services.
     *
     * @param branchCommandService service used to execute branch write operations
     * @param branchQueryService service used to execute branch read operations
     */
    public BranchesController(
            BranchCommandService branchCommandService,
            BranchQueryService branchQueryService
    ) {
        this.branchCommandService = branchCommandService;
        this.branchQueryService = branchQueryService;
    }

    /**
     * Gets branches using optional query parameters.
     *
     * If accountId is provided, branches are filtered by account.
     * If accountId is not provided, all branches are returned.
     *
     * @param accountId optional account identifier
     * @return list of branch resources
     */
    @Operation(summary = "Get branches with optional filters")
    @GetMapping
    public ResponseEntity<List<BranchResource>> getAll(
            @RequestParam(required = false) String accountId
    ) {
        var branches = hasValue(accountId)
                ? branchQueryService.handle(new GetBranchesByAccountIdQuery(new AccountId(accountId)))
                : branchQueryService.handle(new GetAllBranchesQuery());

        var resources = branches.stream()
                .map(BranchResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    /**
     * Creates a new branch using multipart/form-data.
     *
     * The account identifier is received as a query parameter to keep the base
     * endpoint under the branches resource.
     *
     * Example:
     * POST /api/v1/branches?accountId=acc-123
     *
     * @param accountId account identifier
     * @param resource multipart form data with branch information and optional image
     * @return created branch resource
     */
    @Operation(summary = "Create branch")
    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BranchResource> create(
            @RequestParam String accountId,
            @Valid @ModelAttribute CreateBranchResource resource
    ) {
        var command = CreateBranchCommandFromResourceAssembler.toCommandFromResource(
                accountId,
                resource
        );

        var branch = branchCommandService.handle(command);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BranchResourceFromEntityAssembler.toResourceFromEntity(branch));
    }

    /**
     * Gets a branch by its identifier.
     *
     * If the branch is not found, a ResponseStatusException is thrown so the
     * GlobalExceptionHandler can return a structured error response.
     *
     * @param branchId branch identifier
     * @return branch resource
     */
    @Operation(summary = "Get branch by ID")
    @GetMapping("/{branchId}")
    public ResponseEntity<BranchResource> getById(@PathVariable String branchId) {
        var branch = branchQueryService.handle(new GetBranchByIdQuery(branchId))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Branch not found: " + branchId
                ));

        return ResponseEntity.ok(BranchResourceFromEntityAssembler.toResourceFromEntity(branch));
    }

    /**
     * Updates an existing branch using multipart/form-data.
     *
     * If no image is provided, the current branch image is preserved.
     *
     * @param branchId branch identifier
     * @param resource multipart form data with updated branch information and optional image
     * @return updated branch resource
     */
    @Operation(summary = "Update branch")
    @PatchMapping(value = "/{branchId}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BranchResource> update(
            @PathVariable String branchId,
            @Valid @ModelAttribute UpdateBranchResource resource
    ) {
        var command = UpdateBranchCommandFromResourceAssembler.toCommandFromResource(
                branchId,
                resource
        );

        var branch = branchCommandService.handle(command)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Branch not found: " + branchId
                ));

        return ResponseEntity.ok(BranchResourceFromEntityAssembler.toResourceFromEntity(branch));
    }

    /**
     * Deactivates a branch.
     *
     * This endpoint performs a logical deletion by changing the branch status.
     *
     * @param branchId branch identifier
     * @return deactivation confirmation
     */
    @Operation(summary = "Deactivate branch")
    @DeleteMapping("/{branchId}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable String branchId) {
        branchCommandService.handle(new DeleteBranchCommand(branchId));

        return ResponseEntity.ok(Map.of(
                "id", branchId,
                "deactivatedAt", Instant.now().toString()
        ));
    }

    private boolean hasValue(String value) {
        return value != null && !value.isBlank();
    }

    /**
     * Updates the operational status of a branch.
     *
     * @param branchId branch identifier
     * @param resource request body with the new status
     * @return updated branch resource
     */
    @Operation(summary = "Update branch status")
    @PatchMapping(value = "/{branchId}/status", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<BranchResource> updateStatus(
            @PathVariable String branchId,
            @Valid @RequestBody UpdateBranchStatusResource resource
    ) {
        var command = new UpdateBranchStatusCommand(branchId, resource.status());

        var branch = branchCommandService.handle(command)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Branch not found: " + branchId
                ));

        return ResponseEntity.ok(BranchResourceFromEntityAssembler.toResourceFromEntity(branch));
    }

}