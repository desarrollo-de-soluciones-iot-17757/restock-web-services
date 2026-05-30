package com.uitopic.restock.platform.resources.interfaces.rest.controllers;

import com.uitopic.restock.platform.resources.domain.model.commands.CreateBranchCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.UpdateBranchInfoCommand;
import com.uitopic.restock.platform.resources.domain.model.queries.GetBranchesByAccountIdQuery;
import com.uitopic.restock.platform.resources.domain.services.BranchCommandService;
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

    @Operation(summary = "Get all branches for an account")
    @GetMapping
    public ResponseEntity<List<BranchResource>> getBranchesByAccountId(@PathVariable @NotNull String accountId) {
        log.debug("GET /api/v1/accounts/{}/branches", accountId);
        var id = SharedValueObjectFromStringAssembler.toAccountIdFromString(accountId);
        var branches = branchQueryService.handle(new GetBranchesByAccountIdQuery(id));
        var resources = branches.stream().map(BranchResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

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

    @Operation(summary = "Update branch info")
    @PatchMapping("/{branchId}")
    public ResponseEntity<BranchResource> updateBranch(@PathVariable @NotNull String accountId,
                                                        @PathVariable @NotNull String branchId,
                                                        @Valid @RequestBody UpdateBranchInfoResource resource) {
        var command = new UpdateBranchInfoCommand(branchId, resource.name(), resource.address(),
                resource.city(), resource.regionOrState(), resource.country(), resource.description());
        return branchCommandService.handle(command)
                .map(b -> ResponseEntity.ok(BranchResourceFromEntityAssembler.toResourceFromEntity(b)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Deactivate a branch (logical delete)")
    @DeleteMapping("/{branchId}")
    public ResponseEntity<Void> deleteBranch(@PathVariable @NotNull String accountId,
                                              @PathVariable @NotNull String branchId) {
        branchCommandService.delete(branchId);
        return ResponseEntity.noContent().build();
    }
}
