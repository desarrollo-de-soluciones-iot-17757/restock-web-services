package com.uitopic.restock.platform.resources.interfaces.rest.controllers;

import com.uitopic.restock.platform.resources.domain.model.commands.SeedSuppliesCommand;
import com.uitopic.restock.platform.resources.domain.model.queries.GetAllSuppliesQuery;
import com.uitopic.restock.platform.resources.domain.services.SupplyCommandService;
import com.uitopic.restock.platform.resources.domain.services.SupplyQueryService;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.CreateSupplyResource;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.SupplyResource;
import com.uitopic.restock.platform.resources.interfaces.rest.transform.SupplyResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/supplies", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Supplies", description = "Supply template management.")
public class SuppliesController {

    private final SupplyCommandService commandService;
    private final SupplyQueryService queryService;

    public SuppliesController(SupplyCommandService commandService, SupplyQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @Operation(summary = "Get all supply templates")
    @GetMapping
    public ResponseEntity<List<SupplyResource>> getAll() {
        var supplies = queryService.handle(new GetAllSuppliesQuery());
        return ResponseEntity.ok(supplies.stream().map(SupplyResourceFromEntityAssembler::toResourceFromEntity).toList());
    }

    @Operation(summary = "Get supply template by ID")
    @GetMapping("/{id}")
    public ResponseEntity<SupplyResource> getById(@PathVariable String id) {
        return queryService.findById(id)
                .map(s -> ResponseEntity.ok(SupplyResourceFromEntityAssembler.toResourceFromEntity(s)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a supply template")
    @PostMapping
    public ResponseEntity<SupplyResource> create(@Valid @RequestBody CreateSupplyResource resource) {
        var command = new SeedSuppliesCommand(resource.name(), resource.description(),
                resource.category(), resource.isPerishable());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SupplyResourceFromEntityAssembler.toResourceFromEntity(commandService.handle(command)));
    }

    @Operation(summary = "Update a supply template")
    @PutMapping("/{id}")
    public ResponseEntity<SupplyResource> update(@PathVariable String id,
                                                  @Valid @RequestBody CreateSupplyResource resource) {
        var command = new SeedSuppliesCommand(resource.name(), resource.description(),
                resource.category(), resource.isPerishable());
        return commandService.update(id, command)
                .map(s -> ResponseEntity.ok(SupplyResourceFromEntityAssembler.toResourceFromEntity(s)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a supply template")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable String id) {
        commandService.delete(id);
        return ResponseEntity.ok(Map.of("id", id, "deletedAt", Instant.now().toString()));
    }
}
