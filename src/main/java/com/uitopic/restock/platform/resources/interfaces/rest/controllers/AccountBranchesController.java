package com.uitopic.restock.platform.resources.interfaces.rest.controllers;

import com.uitopic.restock.platform.resources.domain.model.commands.CreateBranchCommand;
import com.uitopic.restock.platform.resources.domain.model.queries.GetBranchesByAccountIdQuery;
import com.uitopic.restock.platform.resources.domain.services.BranchCommandService;
import com.uitopic.restock.platform.resources.domain.model.commands.UpdateBranchInfoCommand;
import com.uitopic.restock.platform.resources.domain.services.BranchQueryService;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.BranchResource;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.CreateBranchResource;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.UpdateBranchInfoResource;
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
 * REST controller for branch operations scoped to a specific account.
 *
 * <p>Exposes endpoints under {@code /api/v1/accounts/{accountId}/branches} for listing,
 * creating, updating, and logically deleting branches within an account context.
 * The {@code accountId} path variable is used to scope all operations and to verify
 * ownership on update and delete requests.
 *
 * <p>Command handling is delegated to {@link BranchCommandService} and query handling
 * to {@link BranchQueryService}. The controller only maps resources to commands and
 * formats responses via {@link BranchResourceFromEntityAssembler}.
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/accounts/{accountId}/branches", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Account Branches", description = "Branch lifecycle management scoped to an account.")
public class AccountBranchesController {

    private final BranchCommandService branchCommandService;
    private final BranchQueryService branchQueryService;

    /**
     * Constructs an {@code AccountBranchesController} with the required services.
     *
     * @param branchCommandService the service for handling branch write operations
     * @param branchQueryService   the service for handling branch read operations
     */
    public AccountBranchesController(BranchCommandService branchCommandService, BranchQueryService branchQueryService) {
        this.branchCommandService = branchCommandService;
        this.branchQueryService = branchQueryService;
    }

    /**
     * Retrieves all branches for the specified account, with optional state filtering and pagination.
     *
     * @param accountId the unique identifier of the account
     * @param state     optional status filter (e.g., {@code "ACTIVE"} or {@code "INACTIVE"})
     * @param page      zero-based page index (default 0)
     * @param size      maximum number of results per page (default 20)
     * @return 200 with a list of {@link BranchResource} DTOs
     */
    @Operation(summary = "Get branches for an account with optional filters and pagination")
    @GetMapping
    public ResponseEntity<List<BranchResource>> getBranchesByAccountId(@PathVariable @NotNull String accountId,
                                                                       @RequestParam(required = false) String state,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "20") int size) {
        log.debug("GET /api/v1/accounts/{}/branches state={} page={} size={}", accountId, state, page, size);
        var id = SharedValueObjectFromStringAssembler.toAccountIdFromString(accountId);
        var branches = branchQueryService.handle(new GetBranchesByAccountIdQuery(id), state, page, size);
        var resources = branches.stream().map(BranchResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    /**
     * Creates a new branch under the specified account.
     *
     * @param accountId the unique identifier of the account that will own the branch
     * @param resource  the request body containing the branch creation data
     * @return 201 Created with the newly created {@link BranchResource}
     */
    @Operation(summary = "Create a branch for an account")
    @PostMapping
    public ResponseEntity<BranchResource> createBranch(@PathVariable @NotNull String accountId,
                                                        @Valid @RequestBody CreateBranchResource resource) {
        log.info("POST /api/v1/accounts/{}/branches — name: {}", accountId, resource.name());
        var command = new CreateBranchCommand(accountId, resource.name(), resource.address(),
                resource.city(), resource.stateOrRegion(), resource.country(), resource.imageUrl(), resource.description());
        var branch = branchCommandService.handle(command);
        log.info("Branch created — ID: {}", branch.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BranchResourceFromEntityAssembler.toResourceFromEntity(branch));
    }

    /**
     * Fully updates the information of a branch belonging to the specified account.
     *
     * @param accountId the unique identifier of the account that owns the branch
     * @param branchId  the unique identifier of the branch to update
     * @param resource  the request body containing the updated branch data
     * @return 200 with the updated {@link BranchResource}, or 404 if not found or not owned by the account
     */
    @Operation(summary = "Update branch info for an account")
    @PutMapping("/{branchId}")
    public ResponseEntity<BranchResource> updateBranch(@PathVariable @NotNull String accountId,
                                                       @PathVariable @NotNull String branchId,
                                                       @Valid @RequestBody UpdateBranchInfoResource resource) {
        log.info("PUT /api/v1/accounts/{}/branches/{}", accountId, branchId);
        var expectedAccountId = SharedValueObjectFromStringAssembler.toAccountIdFromString(accountId);
        var command = new UpdateBranchInfoCommand(branchId, resource.name(), resource.address(), resource.city(), resource.regionOrState(), resource.country(), resource.description());
        var updated = branchCommandService.handle(command);
        return updated
                .filter(b -> b.getAccountId() != null && b.getAccountId().equals(expectedAccountId))
                .map(b -> ResponseEntity.ok(BranchResourceFromEntityAssembler.toResourceFromEntity(b)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Partially updates the information of a branch belonging to the specified account.
     * Only non-null fields in the request body are applied.
     *
     * @param accountId the unique identifier of the account that owns the branch
     * @param branchId  the unique identifier of the branch to patch
     * @param resource  the request body containing the fields to update
     * @return 200 with the updated {@link BranchResource}, or 404 if not found or not owned by the account
     */
    @Operation(summary = "Patch branch info for an account (partial update)")
    @PatchMapping("/{branchId}")
    public ResponseEntity<BranchResource> patchBranch(@PathVariable @NotNull String accountId,
                                                      @PathVariable @NotNull String branchId,
                                                      @RequestBody UpdateBranchInfoResource resource) {
        log.info("PATCH /api/v1/accounts/{}/branches/{}", accountId, branchId);
        var expectedAccountId = SharedValueObjectFromStringAssembler.toAccountIdFromString(accountId);
        var command = new UpdateBranchInfoCommand(branchId, resource.name(), resource.address(), resource.city(), resource.regionOrState(), resource.country(), resource.description());
        var updated = branchCommandService.handle(command);
        return updated
                .filter(b -> b.getAccountId() != null && b.getAccountId().equals(expectedAccountId))
                .map(b -> ResponseEntity.ok(BranchResourceFromEntityAssembler.toResourceFromEntity(b)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Logically deletes a branch belonging to the specified account.
     * Verifies account ownership before delegating to the command service.
     *
     * @param accountId the unique identifier of the account that owns the branch
     * @param branchId  the unique identifier of the branch to delete
     * @return 204 No Content on success, or 404 if not found or not owned by the account
     */
    @Operation(summary = "Delete a branch (logical) under an account")
    @DeleteMapping("/{branchId}")
    public ResponseEntity<Void> deleteBranch(@PathVariable @NotNull String accountId,
                                             @PathVariable @NotNull String branchId) {
        log.info("DELETE /api/v1/accounts/{}/branches/{}", accountId, branchId);
        var expectedAccountId = SharedValueObjectFromStringAssembler.toAccountIdFromString(accountId);
        var branchOpt = branchQueryService.findById(branchId);
        if (branchOpt.isEmpty() || branchOpt.get().getAccountId() == null || !branchOpt.get().getAccountId().equals(expectedAccountId)) {
            log.warn("Branch {} not found or does not belong to account {}", branchId, accountId);
            return ResponseEntity.notFound().build();
        }
        branchCommandService.delete(branchId);
        log.info("Branch {} deleted (logical) for account {}", branchId, accountId);
        return ResponseEntity.noContent().build();
    }
}
