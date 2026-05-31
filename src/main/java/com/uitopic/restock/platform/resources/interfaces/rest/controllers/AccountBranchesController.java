package com.uitopic.restock.platform.resources.interfaces.rest.controllers;

import com.uitopic.restock.platform.resources.domain.model.queries.GetBranchesByAccountIdQuery;
import com.uitopic.restock.platform.resources.domain.services.BranchCommandService;
import com.uitopic.restock.platform.resources.domain.services.BranchQueryService;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.BranchResource;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.CreateBranchResource;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.UpdateBranchInfoResource;
import com.uitopic.restock.platform.resources.interfaces.rest.transform.BranchResourceFromEntityAssembler;
import com.uitopic.restock.platform.resources.interfaces.rest.transform.CreateBranchCommandFromResourceAssembler;
import com.uitopic.restock.platform.resources.interfaces.rest.transform.UpdateBranchInfoCommandFromResourceAssembler;
import com.uitopic.restock.platform.shared.interfaces.rest.transform.SharedValueObjectFromStringAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.beans.PropertyEditorSupport;
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

    public AccountBranchesController(BranchCommandService branchCommandService, BranchQueryService branchQueryService) {
        this.branchCommandService = branchCommandService;
        this.branchQueryService = branchQueryService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(MultipartFile.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(null);
            }
        });
    }

/**
 * REST controller handling account-scoped branch operations within the resources bounded context.
 *
 * <p>Exposes endpoints under {@code /api/v1/accounts/{accountId}/branches} for creating
 * branches (including optional image upload) and listing all branches for an account
 * with optional status filtering and pagination.
 *
 * <p>Command handling is delegated to {@link BranchCommandService} and query handling
 * to {@link BranchQueryService}. The controller only maps resources to commands and
 * formats responses via {@link BranchResourceFromEntityAssembler}.
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
     * Creates a new branch under the specified account.
     * Accepts multipart/form-data to support optional image upload.
     *
     * @param accountId the unique identifier of the account that will own the branch
     * @param resource  the multipart form data containing the branch creation data
     * @return 201 Created with the newly created {@link BranchResource}
     */
    @Operation(summary = "Create a branch for an account")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BranchResource> createBranch(@PathVariable @NotNull String accountId,
                                                       @Valid @ModelAttribute CreateBranchResource resource) {
        log.info("POST /api/v1/accounts/{}/branches — name: {}", accountId, resource.name());
        var command = CreateBranchCommandFromResourceAssembler.ToCommandFromResource(resource, accountId);
        var branch = branchCommandService.handle(command);
        log.info("Branch created — ID: {}", branch.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BranchResourceFromEntityAssembler.toResourceFromEntity(branch));
    }

    /**
     * Fully updates the information of a branch belonging to the specified account.
     * Accepts multipart/form-data to support optional image upload.
     *
     * @param accountId the unique identifier of the account that owns the branch
     * @param branchId  the unique identifier of the branch to update
     * @param resource  the multipart form data containing the updated branch data
     * @return 200 with the updated {@link BranchResource}, or 404 if not found or not owned by the account
     */
    @Operation(summary = "Update branch info for an account")
    @PutMapping(value = "/{branchId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BranchResource> updateBranch(@PathVariable @NotNull String accountId,
                                                       @PathVariable @NotNull String branchId,
                                                       @ModelAttribute UpdateBranchInfoResource resource) {
        log.info("PUT /api/v1/accounts/{}/branches/{}", accountId, branchId);
        var expectedAccountId = SharedValueObjectFromStringAssembler.toAccountIdFromString(accountId);
        var command = UpdateBranchInfoCommandFromResourceAssembler.ToCommandFromResource(resource, branchId);
        var updated = branchCommandService.handle(command);
        return updated
                .filter(b -> b.getAccountId() != null && b.getAccountId().equals(expectedAccountId))
                .map(b -> ResponseEntity.ok(BranchResourceFromEntityAssembler.toResourceFromEntity(b)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Partially updates the information of a branch belonging to the specified account.
     * Only non-null fields in the request body are applied.
     * Accepts multipart/form-data to support optional image upload.
     *
     * @param accountId the unique identifier of the account that owns the branch
     * @param branchId  the unique identifier of the branch to patch
     * @param resource  the multipart form data containing the fields to update
     * @return 200 with the updated {@link BranchResource}, or 404 if not found or not owned by the account
     */
    @Operation(summary = "Patch branch info for an account (partial update)")
    @PatchMapping(value = "/{branchId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BranchResource> patchBranch(@PathVariable @NotNull String accountId,
                                                      @PathVariable @NotNull String branchId,
                                                      @ModelAttribute UpdateBranchInfoResource resource) {
        log.info("PATCH /api/v1/accounts/{}/branches/{}", accountId, branchId);
        var expectedAccountId = SharedValueObjectFromStringAssembler.toAccountIdFromString(accountId);
        var command = UpdateBranchInfoCommandFromResourceAssembler.ToCommandFromResource(resource, branchId);
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
        if (branchOpt.isEmpty() || branchOpt.get().getAccountId() == null
                || !branchOpt.get().getAccountId().equals(expectedAccountId)) {
            log.warn("Branch {} not found or does not belong to account {}", branchId, accountId);
            return ResponseEntity.notFound().build();
        }
        branchCommandService.delete(branchId);
        log.info("Branch {} deleted (logical) for account {}", branchId, accountId);
        return ResponseEntity.noContent().build();
    }
}
