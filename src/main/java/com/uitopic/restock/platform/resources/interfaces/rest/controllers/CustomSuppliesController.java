package com.uitopic.restock.platform.resources.interfaces.rest.controllers;

import com.uitopic.restock.platform.resources.domain.model.commands.DeleteCustomSupplyCommand;
import com.uitopic.restock.platform.resources.domain.model.queries.GetAllCustomSuppliesQuery;
import com.uitopic.restock.platform.resources.domain.model.queries.GetCustomSuppliesByAccountIdQuery;
import com.uitopic.restock.platform.resources.domain.model.queries.GetCustomSupplyByIdQuery;
import com.uitopic.restock.platform.resources.domain.services.CustomSupplyCommandService;
import com.uitopic.restock.platform.resources.domain.services.CustomSupplyQueryService;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.CreateCustomSupplyResource;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.CustomSupplyResource;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.UpdateCustomSupplyResource;
import com.uitopic.restock.platform.resources.interfaces.rest.transform.CreateCustomSupplyCommandFromResourceAssembler;
import com.uitopic.restock.platform.resources.interfaces.rest.transform.CustomSupplyResourceFromEntityAssembler;
import com.uitopic.restock.platform.resources.interfaces.rest.transform.UpdateCustomSupplyCommandFromResourceAssembler;
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
 * REST controller for custom supply management.
 *
 * Provides CRUD operations and account-scoped queries using optional query
 * parameters. Endpoints that receive images use multipart/form-data.
 */
@RestController
@RequestMapping(value = "/api/v1/custom-supplies", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Custom Supplies", description = "Custom supply management and query endpoints.")
public class CustomSuppliesController {

    private final CustomSupplyCommandService customSupplyCommandService;
    private final CustomSupplyQueryService customSupplyQueryService;

    /**
     * Creates a CustomSuppliesController with the required command and query services.
     *
     * @param customSupplyCommandService service used to execute custom supply write operations
     * @param customSupplyQueryService service used to execute custom supply read operations
     */
    public CustomSuppliesController(
            CustomSupplyCommandService customSupplyCommandService,
            CustomSupplyQueryService customSupplyQueryService
    ) {
        this.customSupplyCommandService = customSupplyCommandService;
        this.customSupplyQueryService = customSupplyQueryService;
    }

    /**
     * Gets custom supplies using optional query parameters.
     *
     * If accountId is provided, only custom supplies from that account are returned.
     * If no accountId is provided, all custom supplies are returned.
     *
     * @param accountId optional account identifier
     * @return list of custom supply resources
     */
    @Operation(summary = "Get custom supplies with optional filters")
    @GetMapping
    public ResponseEntity<List<CustomSupplyResource>> getAll(
            @RequestParam(required = false) String accountId
    ) {
        var customSupplies = hasValue(accountId)
                ? customSupplyQueryService.handle(new GetCustomSuppliesByAccountIdQuery(new AccountId(accountId)))
                : customSupplyQueryService.handle(new GetAllCustomSuppliesQuery());

        var resources = customSupplies.stream()
                .map(CustomSupplyResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    /**
     * Creates a new custom supply using multipart/form-data.
     *
     * The account identifier is received as a query parameter to keep the base
     * endpoint under the custom supplies resource.
     *
     * Example:
     * POST /api/v1/custom-supplies?accountId=acc-123
     *
     * @param accountId account identifier
     * @param resource multipart form data with custom supply information and optional image
     * @return created custom supply resource
     */
    @Operation(summary = "Create custom supply")
    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CustomSupplyResource> create(
            @RequestParam String accountId,
            @Valid @ModelAttribute CreateCustomSupplyResource resource
    ) {
        var command = CreateCustomSupplyCommandFromResourceAssembler.toCommandFromResource(
                accountId,
                resource
        );

        var customSupply = customSupplyCommandService.handle(command);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CustomSupplyResourceFromEntityAssembler.toResourceFromEntity(customSupply));
    }

    /**
     * Gets a custom supply by its identifier.
     *
     * If the custom supply is not found, a ResponseStatusException is thrown so
     * the GlobalExceptionHandler can return a structured error response.
     *
     * @param customSupplyId custom supply identifier
     * @return custom supply resource
     */
    @Operation(summary = "Get custom supply by ID")
    @GetMapping("/{customSupplyId}")
    public ResponseEntity<CustomSupplyResource> getById(@PathVariable String customSupplyId) {
        var customSupply = customSupplyQueryService.handle(new GetCustomSupplyByIdQuery(customSupplyId))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Custom supply not found: " + customSupplyId
                ));

        return ResponseEntity.ok(CustomSupplyResourceFromEntityAssembler.toResourceFromEntity(customSupply));
    }

    /**
     * Updates an existing custom supply using multipart/form-data.
     *
     * If no image is provided, the current custom supply image is preserved.
     *
     * @param customSupplyId custom supply identifier
     * @param resource multipart form data with updated custom supply information and optional image
     * @return updated custom supply resource
     */
    @Operation(summary = "Update custom supply")
    @PatchMapping(value = "/{customSupplyId}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CustomSupplyResource> update(
            @PathVariable String customSupplyId,
            @Valid @ModelAttribute UpdateCustomSupplyResource resource
    ) {
        var command = UpdateCustomSupplyCommandFromResourceAssembler.toCommandFromResource(
                customSupplyId,
                resource
        );

        var customSupply = customSupplyCommandService.handle(command)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Custom supply not found: " + customSupplyId
                ));

        return ResponseEntity.ok(CustomSupplyResourceFromEntityAssembler.toResourceFromEntity(customSupply));
    }

    /**
     * Deletes a custom supply by its identifier.
     *
     * @param customSupplyId custom supply identifier
     * @return deletion confirmation
     */
    @Operation(summary = "Delete custom supply")
    @DeleteMapping("/{customSupplyId}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable String customSupplyId) {
        customSupplyCommandService.handle(new DeleteCustomSupplyCommand(customSupplyId));

        return ResponseEntity.ok(Map.of(
                "id", customSupplyId,
                "deletedAt", Instant.now().toString()
        ));
    }

    private boolean hasValue(String value) {
        return value != null && !value.isBlank();
    }
}