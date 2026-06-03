package com.uitopic.restock.platform.resources.interfaces.rest.controllers;

import com.uitopic.restock.platform.resources.domain.model.queries.GetAllSuppliesQuery;
import com.uitopic.restock.platform.resources.domain.model.queries.GetAllSupplyCategoriesQuery;
import com.uitopic.restock.platform.resources.domain.model.queries.GetSupplyByIdQuery;
import com.uitopic.restock.platform.resources.domain.services.SupplyQueryService;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.SupplyResource;
import com.uitopic.restock.platform.resources.interfaces.rest.transform.SupplyResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller for supply catalog queries.
 *
 * Supplies are base catalog items loaded from a JSON seed and used as templates
 * for custom supplies.
 */
@RestController
@RequestMapping(value = "/api/v1/supplies", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Supplies", description = "Supply catalog queries.")
public class SuppliesController {

    private final SupplyQueryService supplyQueryService;

    /**
     * Creates a SuppliesController with the required query service.
     *
     * @param supplyQueryService service used to execute supply read operations
     */
    public SuppliesController(SupplyQueryService supplyQueryService) {
        this.supplyQueryService = supplyQueryService;
    }

    /**
     * Gets all supply templates.
     *
     * @return list of supply resources
     */
    @Operation(summary = "Get all supplies")
    @GetMapping
    public ResponseEntity<List<SupplyResource>> getAll() {
        var supplies = supplyQueryService.handle(new GetAllSuppliesQuery());

        var resources = supplies.stream()
                .map(SupplyResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    /**
     * Gets all available supply categories.
     *
     * @return list of category names
     */
    @Operation(summary = "Get all supply categories")
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        var categories = supplyQueryService.handle(new GetAllSupplyCategoriesQuery());
        return ResponseEntity.ok(categories);
    }

    /**
     * Gets a supply template by its identifier.
     *
     * If the supply is not found, a ResponseStatusException is thrown so the
     * GlobalExceptionHandler can return a structured error response.
     *
     * @param id supply identifier
     * @return supply resource
     */
    @Operation(summary = "Get supply by ID")
    @GetMapping("/{id}")
    public ResponseEntity<SupplyResource> getById(@PathVariable String id) {
        var supply = supplyQueryService.handle(new GetSupplyByIdQuery(id))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Supply not found: " + id
                ));

        return ResponseEntity.ok(SupplyResourceFromEntityAssembler.toResourceFromEntity(supply));
    }
}