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
}
