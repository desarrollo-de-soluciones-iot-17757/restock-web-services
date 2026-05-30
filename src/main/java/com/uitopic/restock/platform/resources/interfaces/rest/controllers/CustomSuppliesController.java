package com.uitopic.restock.platform.resources.interfaces.rest.controllers;

import com.uitopic.restock.platform.resources.domain.model.commands.CreateCustomSupplyCommand;
import com.uitopic.restock.platform.resources.domain.services.CustomSupplyCommandService;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.*;
import com.uitopic.restock.platform.resources.interfaces.rest.transform.CustomSupplyResourceFromEntityAssembler;
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
@RequestMapping(value = "/api/v1/custom-supplies", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Custom Supplies", description = "Custom supply CRUD operations.")
public class CustomSuppliesController {

    private final CustomSupplyCommandService commandService;

    public CustomSuppliesController(CustomSupplyCommandService commandService) {
        this.commandService = commandService;
    }

    @Operation(summary = "Create a custom supply")
    @PostMapping
    public ResponseEntity<CustomSupplyResource> create(@Valid @RequestBody CreateCustomSupplyResource resource) {
        var command = new CreateCustomSupplyCommand(resource.accountId(), resource.supplyId(), resource.name(),
                resource.description(), resource.unitPrice(), resource.supplyContent(),
                resource.unitMeasurement(), resource.minimumStock(), resource.imageUrl());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CustomSupplyResourceFromEntityAssembler.toResourceFromEntity(commandService.handle(command)));
    }

    @Operation(summary = "Update a custom supply")
    @PutMapping("/{id}")
    public ResponseEntity<CustomSupplyResource> update(@PathVariable String id,
                                                        @Valid @RequestBody CreateCustomSupplyResource resource) {
        var command = new CreateCustomSupplyCommand(resource.accountId(), resource.supplyId(), resource.name(),
                resource.description(), resource.unitPrice(), resource.supplyContent(),
                resource.unitMeasurement(), resource.minimumStock(), resource.imageUrl());
        return commandService.update(id, command)
                .map(cs -> ResponseEntity.ok(CustomSupplyResourceFromEntityAssembler.toResourceFromEntity(cs)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a custom supply")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable String id) {
        commandService.delete(id);
        return ResponseEntity.ok(Map.of("id", id, "deletedAt", Instant.now().toString()));
    }
}
