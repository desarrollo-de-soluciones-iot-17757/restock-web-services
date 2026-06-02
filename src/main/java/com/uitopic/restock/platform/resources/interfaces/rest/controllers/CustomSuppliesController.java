package com.uitopic.restock.platform.resources.interfaces.rest.controllers;

import com.uitopic.restock.platform.resources.domain.model.commands.UpdateCustomSupplyPerishableCommand;
import com.uitopic.restock.platform.resources.domain.services.CustomSupplyCommandService;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.*;
import com.uitopic.restock.platform.resources.interfaces.rest.transform.CreateCustomSupplyCommandFromResourceAssembler;
import com.uitopic.restock.platform.resources.interfaces.rest.transform.CustomSupplyResourceFromEntityAssembler;
import com.uitopic.restock.platform.resources.interfaces.rest.transform.UpdateCustomSupplyCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller for custom supply CRUD operations not scoped to an account
 * path.
 *
 * <p>
 * Exposes endpoints under {@code /api/v1/custom-supplies} for creating,
 * updating,
 * and deleting custom supplies. Account-scoped listing is handled separately by
 * {@link AccountCustomSuppliesController}.
 *
 * <p>
 * Command handling is delegated to {@link CustomSupplyCommandService}. The
 * controller
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
     * Constructs a {@code CustomSuppliesController} with the required command
     * service.
     *
     * @param commandService the service for handling custom supply write operations
     */
    public CustomSuppliesController(CustomSupplyCommandService commandService) {
        this.commandService = commandService;
    }

    /**
     * Creates a new custom supply for the account specified in the request.
     * Accepts multipart/form-data with a JSON part for the text data and an
     * optional file part for the image.
     *
     * @param resource the JSON part containing the custom supply text data
     * @param image    optional image file sent as multipart
     * @return 201 Created with the newly created {@link CustomSupplyResource}
     */
    @Operation(summary = "Create a custom supply")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CustomSupplyResource> create(
            @Valid @ModelAttribute CreateCustomSupplyResource resource) {
        log.info("POST /api/v1/custom-supplies — creating supply: {}", resource.name());
        var command = CreateCustomSupplyCommandFromResourceAssembler.toCommandFromResource(resource);
        var customSupply = commandService.handle(command);
        log.info("Custom supply created successfully — ID: {}", customSupply.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CustomSupplyResourceFromEntityAssembler.toResourceFromEntity(customSupply));
    }

    /**
     * Fully updates an existing custom supply with the data provided in the
     * request.
     * Accepts multipart/form-data to support optional image upload via Cloudinary.
     *
     * @param id       the unique identifier of the custom supply to update
     * @param resource the multipart form data containing the updated supply data
     * @return 200 with the updated {@link CustomSupplyResource}, or 404 if not found
     */
    @Operation(summary = "Update a custom supply")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CustomSupplyResource> update(@PathVariable String id,
                                                        @Valid @ModelAttribute UpdateCustomSupplyResource resource) {
        log.info("PUT /api/v1/custom-supplies/{}", id);
        var command = UpdateCustomSupplyCommandFromResourceAssembler.toCommandFromResource(resource);
        return commandService.update(id, command)
                .map(cs -> {
                    log.info("Custom supply updated successfully — ID: {}", id);
                    return ResponseEntity.ok(CustomSupplyResourceFromEntityAssembler.toResourceFromEntity(cs));
                })
                .orElseGet(() -> {
                    log.warn("Custom supply not found for update — ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    /**
     * Patches the perishable status of a custom supply.
     *
     * @param id       the unique identifier of the custom supply
     * @param resource the request body containing the new perishable flag
     * @return 200 with the updated {@link CustomSupplyResource}, or 404 if not
     *         found
     */
    @Operation(summary = "Update perishable status of a custom supply")
    @PatchMapping(value = "/{id}/perishable", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomSupplyResource> updatePerishable(
            @PathVariable String id,
            @RequestBody UpdateCustomSupplyPerishableResource resource) {
        log.info("PATCH /api/v1/custom-supplies/{}/perishable — isPerishable: {}", id, resource.isPerishable());
        var command = new UpdateCustomSupplyPerishableCommand(id, resource.isPerishable());
        return commandService.updatePerishable(command)
                .map(cs -> {
                    log.info("Perishable status updated — ID: {}", id);
                    return ResponseEntity.ok(CustomSupplyResourceFromEntityAssembler.toResourceFromEntity(cs));
                })
                .orElseGet(() -> {
                    log.warn("Custom supply not found for perishable update — ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
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
        log.info("DELETE /api/v1/custom-supplies/{}", id);
        commandService.delete(id);
        log.info("Custom supply deleted — ID: {}", id);
        return ResponseEntity.ok(Map.of("id", id, "deletedAt", Instant.now().toString()));
    }
}
