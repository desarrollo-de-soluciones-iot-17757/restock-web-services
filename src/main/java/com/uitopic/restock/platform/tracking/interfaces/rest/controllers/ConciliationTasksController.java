package com.uitopic.restock.platform.tracking.interfaces.rest.controllers;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.BranchId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.DeviceId;
import com.uitopic.restock.platform.tracking.domain.model.commands.ResolveConciliationTaskCommand;
import com.uitopic.restock.platform.tracking.domain.model.queries.GetConciliationTaskByIdQuery;
import com.uitopic.restock.platform.tracking.domain.model.queries.GetConciliationTasksByAccountIdQuery;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.ConciliationTaskStatus;
import com.uitopic.restock.platform.tracking.domain.services.ConciliationTaskCommandService;
import com.uitopic.restock.platform.tracking.domain.services.ConciliationTaskQueryService;
import com.uitopic.restock.platform.tracking.interfaces.rest.resources.ConciliationTaskResource;
import com.uitopic.restock.platform.tracking.interfaces.rest.resources.ResolveConciliationTaskResource;
import com.uitopic.restock.platform.tracking.interfaces.rest.transform.ConciliationTaskResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller for conciliation task management.
 *
 * <p>
 * Provides endpoints for listing conciliation tasks by account, retrieving a
 * task by its identifier, and resolving pending tasks through administrator
 * actions. Query parameters are used for filtering so the endpoint structure
 * remains consistent with other resource controllers.
 */
@RestController
@RequestMapping(value = "/api/v1/conciliation-tasks", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Conciliation task", description = "Conciliation task query and resolution endpoints.")
@RequiredArgsConstructor
public class ConciliationTasksController {
    private final ConciliationTaskQueryService conciliationTaskQueryService;
    private final ConciliationTaskCommandService conciliationTaskCommandService;

    /**
     * Gets conciliation tasks for an account with optional filters.
     *
     * @param accountId account identifier used to scope the query
     * @param status optional conciliation task status filter
     * @param customSupplyId optional custom supply identifier filter
     * @param branchId optional branch identifier filter
     * @param deviceId optional device identifier filter
     * @return list of conciliation task resources matching the requested filters
     */
    @Operation(summary = "Gets conciliation tasks for an account with optional filters.")
    @GetMapping
    public ResponseEntity<List<ConciliationTaskResource>> getByAccount(
            @RequestParam String accountId,
            @RequestParam(required = false) ConciliationTaskStatus status,
            @RequestParam(required = false) String customSupplyId,
            @RequestParam(required = false) String branchId,
            @RequestParam(required = false) String deviceId
    ) {
        var query = new GetConciliationTasksByAccountIdQuery(
                new AccountId(accountId),
                status,
                customSupplyId,
                hasValue(branchId) ? new BranchId(branchId) : null,
                hasValue(deviceId) ? new DeviceId(deviceId) : null
        );
        var resources = conciliationTaskQueryService
                .handle(query)
                .stream()
                .map(ConciliationTaskResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    /**
     * Gets a conciliation task by its identifier.
     *
     * @param conciliationTaskId conciliation task identifier
     * @return conciliation task resource
     */
    @Operation(summary = "Get a conciliation task by ID")
    @GetMapping("/{conciliationTaskId}")
    public ResponseEntity<ConciliationTaskResource> getById(
            @PathVariable String conciliationTaskId
    ) {
        var task = conciliationTaskQueryService.handle(new GetConciliationTaskByIdQuery(conciliationTaskId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conciliation task not found"));
        return ResponseEntity.ok(ConciliationTaskResourceFromEntityAssembler.toResourceFromEntity(task));
    }

    /**
     * Resolves a pending conciliation task using a manual resolution action.
     *
     * @param conciliationTaskId conciliation task identifier
     * @param resource request body containing the selected resolution action,
     *                 reason, justification, and optional updated justified
     *                 withdrawn stock value
     * @return resolved conciliation task resource
     */
    @Operation(summary = "Resolves a pending conciliation task using a manual resolution action.")
    @PostMapping(value = "/{conciliationTaskId}/resolve", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<ConciliationTaskResource> resolve(
            @PathVariable String conciliationTaskId,
            @Valid @RequestBody ResolveConciliationTaskResource resource
    ) {
        var command = new ResolveConciliationTaskCommand(
                conciliationTaskId,
                resource.resolvedByUserId(),
                resource.resolutionAction(),
                resource.resolutionReason(),
                resource.resolutionJustification(),
                resource.newJustifiedWithdrawnStock()
        );
        var task = conciliationTaskCommandService.handle(command);
        return ResponseEntity.ok(ConciliationTaskResourceFromEntityAssembler.toResourceFromEntity(task));
    }

    private boolean hasValue(String value) {
        return value != null && !value.isBlank();
    }
}
