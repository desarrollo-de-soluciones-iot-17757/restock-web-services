package com.uitopic.restock.platform.tracking.interfaces.rest.controllers;

import com.uitopic.restock.platform.tracking.domain.model.queries.GetDiscrepanciesQuery;
import com.uitopic.restock.platform.tracking.domain.model.queries.GetDiscrepancyByIdQuery;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.DiscrepancyStatus;
import com.uitopic.restock.platform.tracking.domain.services.DiscrepancyQueryService;
import com.uitopic.restock.platform.tracking.interfaces.rest.resources.DiscrepancyResource;
import com.uitopic.restock.platform.tracking.interfaces.rest.transform.DiscrepancyResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller for discrepancy query and management operations.
 */
@RestController
@RequestMapping(value = "/api/v1/discrepancies", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Discrepancies", description = "Discrepancy query endpoints.")
@RequiredArgsConstructor
public class DiscrepanciesController {
    private final DiscrepancyQueryService discrepancyQueryService;

    /**
     * Gets discrepancies with optional status filtering.
     *
     * @param status optional discrepancy status filter
     * @return list of matching discrepancy resources
     */
    @Operation(summary = "Gets discrepancies with optional status filtering.")
    @GetMapping
    public ResponseEntity<List<DiscrepancyResource>> getDiscrepancies(
            @RequestParam(required = false) DiscrepancyStatus status
    ) {
        var resources = discrepancyQueryService
                .handle(new GetDiscrepanciesQuery(status))
                .stream()
                .map(DiscrepancyResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    /**
     * Gets a discrepancy by its identifier.
     *
     * @param discrepancyId discrepancy identifier
     * @return discrepancy resource
     */
    @Operation(summary = "Gets a discrepancy by ID.")
    @GetMapping("/{discrepancyId}")
    public ResponseEntity<DiscrepancyResource> getById(
            @PathVariable String discrepancyId
    ) {
        var discrepancy = discrepancyQueryService.handle(new GetDiscrepancyByIdQuery(discrepancyId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Discrepancy not found"));
        return ResponseEntity.ok(DiscrepancyResourceFromEntityAssembler.toResourceFromEntity(discrepancy));
    }
}
