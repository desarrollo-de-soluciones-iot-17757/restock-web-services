package com.uitopic.restock.platform.resources.interfaces.rest.controllers;

import com.uitopic.restock.platform.resources.domain.services.BranchCommandService;
import com.uitopic.restock.platform.resources.domain.services.BranchQueryService;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.BranchResource;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.UpdateBranchImageResource;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.UpdateBranchInfoResource;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.UpdateBranchStatusResource;
import com.uitopic.restock.platform.resources.interfaces.rest.transform.BranchResourceFromEntityAssembler;
import com.uitopic.restock.platform.resources.interfaces.rest.transform.UpdateBranchCommandFromResourceAssembler;
import com.uitopic.restock.platform.resources.interfaces.rest.transform.UpdateBranchImageCommandFromResourceAssembler;
import com.uitopic.restock.platform.resources.interfaces.rest.transform.UpdateBranchInfoCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller for branch operations within the resources bounded context,
 * not scoped to an account path.
 *
 * <p>Exposes endpoints under {@code /api/v1/branches} for retrieving a branch by ID,
 * updating branch info, updating the branch image, and logically deleting a branch.
 * This controller complements {@link AccountBranchesController}, which handles account-scoped
 * branch creation and listing.
 *
 * <p>Command handling is delegated to {@link BranchCommandService} and query handling
 * to {@link BranchQueryService}. The controller only maps resources to commands and
 * formats responses via {@link BranchResourceFromEntityAssembler}.
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/branches", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Branches", description = "Branch management.")
public class BranchesController {

    private final BranchCommandService commandService;
    private final BranchQueryService queryService;

    /**
     * Constructs a {@code BranchesController} with the required services.
     *
     * @param commandService the service for handling branch write operations
     * @param queryService   the service for handling branch read operations
     */
    public BranchesController(BranchCommandService commandService, BranchQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    /**
     * Retrieves a branch by its unique identifier.
     *
     * @param branchId the unique identifier of the branch
     * @return 200 with the {@link BranchResource}, or 404 if not found
     */
    @Operation(summary = "Get branch by ID")
    @GetMapping("/{branchId}")
    public ResponseEntity<BranchResource> getById(@PathVariable String branchId) {
        log.debug("GET /api/v1/branches/{}", branchId);
        return queryService.findById(branchId)
                .map(b -> ResponseEntity.ok(BranchResourceFromEntityAssembler.toResourceFromEntity(b)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates the information of a branch. Accepts multipart/form-data to support
     * optional image upload alongside info fields.
     *
     * @param branchId the unique identifier of the branch to update
     * @param resource the multipart form data containing the fields to update
     * @return 200 with the updated {@link BranchResource}, or 404 if not found
     */
    @Operation(summary = "Update branch info")
    @PutMapping(value = "/{branchId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BranchResource> updateBranchInfo(@PathVariable String branchId,
                                                           @ModelAttribute UpdateBranchInfoResource resource) {
        log.info("PUT /api/v1/branches/{}", branchId);
        var command = UpdateBranchInfoCommandFromResourceAssembler.ToCommandFromResource(resource, branchId);
        return commandService.handle(command)
                .map(b -> ResponseEntity.ok(BranchResourceFromEntityAssembler.toResourceFromEntity(b)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates the image of a branch. Accepts multipart/form-data to support file upload.
     *
     * @param branchId the unique identifier of the branch
     * @param resource the multipart form data containing the new image file
     * @return 200 with the updated {@link BranchResource}, or 404 if not found
     */
    @Operation(summary = "Update branch image")
    @PatchMapping(value = "/{branchId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BranchResource> updateImage(@PathVariable String branchId,
                                                      @ModelAttribute UpdateBranchImageResource resource) {
        log.info("PATCH /api/v1/branches/{}/image", branchId);
        var command = UpdateBranchImageCommandFromResourceAssembler.ToCommandFromResource(resource, branchId);
        return commandService.handle(command)
                .map(b -> ResponseEntity.ok(BranchResourceFromEntityAssembler.toResourceFromEntity(b)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Logically deletes a branch by updating its status to INACTIVE. The branch is not removed from the database,
     * allowing for potential reactivation in the future.
     *
     * @param branchId the unique identifier of the branch to delete
     * @param resource the resource containing the new status (e.g., "INACTIVE")
     * @return 204 No Content if successful, or 404 if the branch is not found
     */
    @Operation(summary = "Delete a branch (logical)")
    @PatchMapping("/{branchId}")
    public ResponseEntity<Void> delete(@PathVariable String branchId, @RequestBody UpdateBranchStatusResource resource) {
        log.info("DELETE /api/v1/branches/{}", branchId);
        var updateBranchStatusCommand = UpdateBranchCommandFromResourceAssembler.ToCommandFromResource(branchId, resource);
        commandService.handle(updateBranchStatusCommand);
        return ResponseEntity.noContent().build();
    }
}