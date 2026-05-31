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

/**
 * REST controller for custom supply CRUD operations not scoped to an account path.
 *
 * <p>Exposes endpoints under {@code /api/v1/custom-supplies} for creating, updating,
 * and deleting custom supplies. Account-scoped listing is handled separately by
 * {@link AccountCustomSuppliesController}.
 *
 * <p>Command handling is delegated to {@link CustomSupplyCommandService}. The controller
 * only maps resources to commands and formats responses via
 * {@link CustomSupplyResourceFromEntityAssembler}.
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/custom-supplies", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Custom Supplies", description = "Custom supply CRUD operations.")
public class CustomSuppliesController {

    private final CustomSupplyCommandService commandService;

    /**
     * Constructs a {@code CustomSuppliesController} with the required command service.
     *
     * @param commandService the service for handling custom supply write operations
     */
    public CustomSuppliesController(CustomSupplyCommandService commandService) {
        this.commandService = commandService;
    }

    /**
     * Creates a new custom supply for the account specified in the request body.
     *
     * @param resource the request body containing all data required to create the custom supply
     * @return 201 Created with the newly created {@link CustomSupplyResource}
     */
    @Operation(summary = "Create a custom supply")
    @PostMapping
    public ResponseEntity<CustomSupplyResource> create(@Valid @RequestBody CreateCustomSupplyResource resource) {
        var command = new CreateCustomSupplyCommand(resource.accountId(), resource.supplyId(), resource.name(),
                resource.description(), resource.unitPrice(), resource.supplyContent(),
                resource.unitMeasurement(), resource.minimumStock(), resource.imageUrl());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CustomSupplyResourceFromEntityAssembler.toResourceFromEntity(commandService.handle(command)));
    }

    /**
     * Fully updates an existing custom supply with the data provided in the request body.
     *
     * @param id       the unique identifier of the custom supply to update
     * @param resource the request body containing the updated supply data
     * @return 200 with the updated {@link CustomSupplyResource}, or 404 if not found
     */
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

    /**
     * Deletes a custom supply by its unique identifier.
     * Deletion is blocked if the supply has active batches with remaining stock.
     *
     * @param id the unique identifier of the custom supply to delete
     * @return 200 with a confirmation map containing the deleted ID and timestamp
     */
    @Operation(summary = "Delete a custom supply")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable String id) {
        commandService.delete(id);
        return ResponseEntity.ok(Map.of("id", id, "deletedAt", Instant.now().toString()));
    }
}
