package com.uitopic.restock.platform.resources.interfaces.rest.controllers;

import com.uitopic.restock.platform.resources.domain.model.commands.UpdateBranchInfoCommand;
import com.uitopic.restock.platform.resources.domain.services.BranchCommandService;
import com.uitopic.restock.platform.resources.domain.services.BranchQueryService;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.BranchResource;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.UpdateBranchImageResource;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.UpdateBranchInfoResource;
import com.uitopic.restock.platform.resources.interfaces.rest.transform.BranchResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller for branch operations not scoped to an account path.
 *
 * <p>Exposes endpoints under {@code /api/v1/branches} for retrieving a branch by ID,
 * partially updating branch info, updating the branch image, and logically deleting a branch.
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
     * Partially updates the information of a branch. Only non-null fields in the request body are applied.
     *
     * @param branchId the unique identifier of the branch to update
     * @param resource the request body containing the fields to update
     * @return 200 with the updated {@link BranchResource}, or 404 if not found
     */
    @Operation(summary = "Update branch info")
    @PatchMapping("/{branchId}")
    public ResponseEntity<BranchResource> updateInfo(@PathVariable String branchId,
                                                     @Valid @RequestBody UpdateBranchInfoResource resource) {
        log.info("PATCH /api/v1/branches/{}", branchId);
        var command = new UpdateBranchInfoCommand(branchId, resource.name(), resource.address(),
                resource.city(), resource.regionOrState(), resource.country(), resource.description());
        return commandService.handle(command)
                .map(b -> ResponseEntity.ok(BranchResourceFromEntityAssembler.toResourceFromEntity(b)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates the image URL of a branch.
     *
     * @param branchId the unique identifier of the branch
     * @param resource the request body containing the new image URL
     * @return 200 with the updated {@link BranchResource}, or 404 if not found
     */
    @Operation(summary = "Update branch image")
    @PatchMapping("/{branchId}/image")
    public ResponseEntity<BranchResource> updateImage(@PathVariable String branchId,
                                                      @RequestBody UpdateBranchImageResource resource) {
        log.info("PATCH /api/v1/branches/{}/image", branchId);
        return commandService.updateImage(branchId, resource.imageUrl())
                .map(b -> ResponseEntity.ok(BranchResourceFromEntityAssembler.toResourceFromEntity(b)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Logically deletes a branch by transitioning its status to inactive.
     *
     * @param branchId the unique identifier of the branch to delete
     * @return 204 No Content on success
     */
    @Operation(summary = "Delete a branch (logical)")
    @DeleteMapping("/{branchId}")
    public ResponseEntity<Void> delete(@PathVariable String branchId) {
        log.info("DELETE /api/v1/branches/{}", branchId);
        commandService.delete(branchId);
        return ResponseEntity.noContent().build();
    }
}
