package com.uitopic.restock.platform.resources.interfaces.rest.controllers;

import com.uitopic.restock.platform.resources.domain.model.commands.CreateBranchCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.UpdateBranchInfoCommand;
import com.uitopic.restock.platform.resources.domain.services.BranchCommandService;
import com.uitopic.restock.platform.resources.domain.services.BranchQueryService;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.*;
import com.uitopic.restock.platform.resources.interfaces.rest.transform.BranchResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/branches", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Branches", description = "Branch management.")
public class BranchesController {

    private final BranchCommandService commandService;
    private final BranchQueryService queryService;

    public BranchesController(BranchCommandService commandService, BranchQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @Operation(summary = "Create a branch")
    @PostMapping
    public ResponseEntity<BranchResource> create(@Valid @RequestBody CreateBranchResource resource) {
        var command = new CreateBranchCommand(resource.accountId(), resource.name(), resource.address(),
                resource.city(),resource.stateOrRegion(), resource.country(), resource.imageUrl(), resource.description());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BranchResourceFromEntityAssembler.toResourceFromEntity(commandService.handle(command)));
    }

    @Operation(summary = "Get branch by ID")
    @GetMapping("/{branchId}")
    public ResponseEntity<BranchResource> getById(@PathVariable String branchId) {
        return queryService.findById(branchId)
                .map(b -> ResponseEntity.ok(BranchResourceFromEntityAssembler.toResourceFromEntity(b)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update branch info")
    @PatchMapping("/{branchId}")
    public ResponseEntity<BranchResource> updateInfo(@PathVariable String branchId,
                                                      @Valid @RequestBody UpdateBranchInfoResource resource) {
        var command = new UpdateBranchInfoCommand(branchId, resource.name(), resource.address(),
                resource.city(), resource.regionOrState(), resource.country(), resource.description());
        return commandService.handle(command)
                .map(b -> ResponseEntity.ok(BranchResourceFromEntityAssembler.toResourceFromEntity(b)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update branch image")
    @PatchMapping("/{branchId}/image")
    public ResponseEntity<BranchResource> updateImage(@PathVariable String branchId,
                                                       @RequestBody UpdateBranchImageResource resource) {
        return commandService.updateImage(branchId, resource.imageUrl())
                .map(b -> ResponseEntity.ok(BranchResourceFromEntityAssembler.toResourceFromEntity(b)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a branch")
    @DeleteMapping("/{branchId}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable String branchId) {
        commandService.delete(branchId);
        return ResponseEntity.ok(Map.of("branchId", branchId, "deletedAt", Instant.now().toString()));
    }
}
