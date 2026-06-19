package com.uitopic.restock.platform.profiles.interfaces.rest.controllers;

import com.uitopic.restock.platform.profiles.domain.model.commands.DeleteBusinessCommand;
import com.uitopic.restock.platform.profiles.domain.model.queries.GetAllBusinessesQuery;
import com.uitopic.restock.platform.profiles.domain.model.queries.GetBusinessByIdQuery;
import com.uitopic.restock.platform.profiles.domain.model.queries.GetBusinessByUserIdQuery;
import com.uitopic.restock.platform.profiles.domain.services.BusinessCommandService;
import com.uitopic.restock.platform.profiles.domain.services.BusinessQueryService;
import com.uitopic.restock.platform.profiles.interfaces.rest.resources.BusinessResource;
import com.uitopic.restock.platform.profiles.interfaces.rest.resources.CreateBusinessResource;
import com.uitopic.restock.platform.profiles.interfaces.rest.resources.UpdateBusinessResource;
import com.uitopic.restock.platform.profiles.interfaces.rest.transform.BusinessResourceFromEntityAssembler;
import com.uitopic.restock.platform.profiles.interfaces.rest.transform.CreateBusinessCommandFromResourceAssembler;
import com.uitopic.restock.platform.profiles.interfaces.rest.transform.UpdateBusinessCommandFromResourceAssembler;
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

@RestController
@RequestMapping(value = "/api/v1/businesses", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Businesses", description = "Business profile management endpoints")
public class BusinessController {

    private final BusinessCommandService businessCommandService;
    private final BusinessQueryService businessQueryService;

    public BusinessController(BusinessCommandService businessCommandService,
                              BusinessQueryService businessQueryService) {
        this.businessCommandService = businessCommandService;
        this.businessQueryService = businessQueryService;
    }

    @Operation(summary = "Get businesses with optional filters")
    @GetMapping
    public ResponseEntity<List<BusinessResource>> getAll(
            @RequestParam(required = false) String userId
    ) {
        var businesses = userId != null && !userId.isBlank()
                ? businessQueryService.handle(new GetBusinessByUserIdQuery(userId))
                : businessQueryService.handle(new GetAllBusinessesQuery());

        return ResponseEntity.ok(businesses.stream()
                .map(BusinessResourceFromEntityAssembler::toResourceFromEntity)
                .toList());
    }

    @Operation(summary = "Create business")
    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BusinessResource> create(@Valid @ModelAttribute CreateBusinessResource resource) {
        var command = CreateBusinessCommandFromResourceAssembler.toCommandFromResource(resource);
        var business = businessCommandService.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BusinessResourceFromEntityAssembler.toResourceFromEntity(business));
    }

    @Operation(summary = "Get business by ID")
    @GetMapping("/{businessId}")
    public ResponseEntity<BusinessResource> getById(@PathVariable String businessId) {
        var business = businessQueryService.handle(new GetBusinessByIdQuery(businessId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Business not found: " + businessId));
        return ResponseEntity.ok(BusinessResourceFromEntityAssembler.toResourceFromEntity(business));
    }

    @Operation(summary = "Update business")
    @PatchMapping(value = "/{businessId}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BusinessResource> update(
            @PathVariable String businessId,
            @Valid @ModelAttribute UpdateBusinessResource resource
    ) {
        var command = UpdateBusinessCommandFromResourceAssembler.toCommandFromResource(businessId, resource);
        var business = businessCommandService.handle(command)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Business not found: " + businessId));
        return ResponseEntity.ok(BusinessResourceFromEntityAssembler.toResourceFromEntity(business));
    }

    @Operation(summary = "Delete business")
    @DeleteMapping("/{businessId}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable String businessId) {
        businessCommandService.handle(new DeleteBusinessCommand(businessId));
        return ResponseEntity.ok(Map.of(
                "id", businessId,
                "deletedAt", Instant.now().toString()
        ));
    }
}
