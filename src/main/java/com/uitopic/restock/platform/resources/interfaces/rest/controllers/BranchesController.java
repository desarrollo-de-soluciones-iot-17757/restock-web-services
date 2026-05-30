package com.uitopic.restock.platform.resources.interfaces.rest.controllers;

import com.uitopic.restock.platform.resources.domain.model.commands.CreateBranchCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.UpdateBranchInfoCommand;
import com.uitopic.restock.platform.resources.domain.services.BranchCommandService;
import com.uitopic.restock.platform.resources.domain.services.BranchQueryService;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.*;
import com.uitopic.restock.platform.resources.interfaces.rest.transform.BranchResourceFromEntityAssembler;
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
 * BranchesController.java
 * This controller manages branch-related operations. It provides endpoints to:
 * - Get branch details by ID.
 * - Update branch information.
 * - Update branch image.
 * - Delete a branch.
 * The controller uses BranchCommandService for handling commands related to branch updates and deletions, and BranchQueryService for fetching branch data. It includes logging for debugging purposes and is annotated for OpenAPI documentation. The endpoints are designed to be RESTful and return appropriate HTTP status codes based on the outcome of the operations.
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/branches", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Branches", description = "Branch management.")
public class BranchesController {

    /** The BranchCommandService is used to handle commands related to branch operations, such as updating branch information, updating branch images, and deleting branches. It encapsulates the business logic for these operations and interacts with the underlying data layer to persist changes. The BranchQueryService is responsible for fetching branch data based on queries, such as retrieving branch details by ID. Both services are injected into the controller through its constructor, allowing the controller to delegate command handling and query processing to these services. */
    private final BranchCommandService commandService;

    /** The BranchQueryService is responsible for handling queries related to branch data retrieval. It provides methods to fetch branch information based on various criteria, such as branch ID. In this controller, it is used to retrieve branch details when the corresponding endpoint is called. The BranchQueryService is injected into the controller through its constructor, allowing the controller to delegate query processing to this service. */
    private final BranchQueryService queryService;

    /** Constructor for BranchesController. This constructor takes two parameters: commandService and queryService. The commandService is used to handle branch update and delete commands, and the queryService is used to fetch branch data. */
    public BranchesController(BranchCommandService commandService, BranchQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    /**
     * Endpoint to retrieve branch details by ID. This method is mapped to a GET request at the path "/api/v1/branches/{branchId}". It takes the branchId as a path variable, validates it, and uses the BranchQueryService to fetch the branch details. If a branch with the specified ID is found, it is transformed into a BranchResource object using the BranchResourceFromEntityAssembler and returned in the response body with an HTTP 200 OK status. If no branch with the given ID exists, an HTTP 404 Not Found status is returned.
     * @param branchId the unique identifier of the branch to retrieve
     * @return a ResponseEntity containing a BranchResource object representing the branch details if found, or an HTTP 404 Not Found status if no branch with the given ID exists
     */
    @Operation(summary = "Get branch by ID")
    @GetMapping("/{branchId}")
    public ResponseEntity<BranchResource> getById(@PathVariable String branchId) {
        return queryService.findById(branchId)
                .map(b -> ResponseEntity.ok(BranchResourceFromEntityAssembler.toResourceFromEntity(b)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint to update branch information. This method is mapped to a PATCH request at the path "/api/v1/branches/{branchId}". It takes the branchId as a path variable and an UpdateBranchInfoResource object as the request body, validates them, and uses the BranchCommandService to handle the update of the branch information. If the update is successful and a branch with the specified ID exists, the updated branch is transformed into a BranchResource object using the BranchResourceFromEntityAssembler and returned in the response body with an HTTP 200 OK status. If no branch with the given ID exists, an HTTP 404 Not Found status is returned.
     * @param branchId the unique identifier of the branch to update
     * @param resource the UpdateBranchInfoResource object containing the necessary information to update the branch
     * @return a ResponseEntity containing a BranchResource object representing the updated branch if the update was successful, or an HTTP 404 Not Found status if no branch with the given ID exists
     */
    @Operation(summary = "Update branch info")
    @PatchMapping("/{branchId}")
    public ResponseEntity<BranchResource> updateInfo(@PathVariable String branchId,
                                                      @Valid @RequestBody UpdateBranchInfoResource resource) {
        var command = new UpdateBranchInfoCommand(branchId, resource.name(), resource.address(),
                resource.city(), resource.regionOrState(), resource.country(), resource.description());
        return commandService.handle(command)
                .map(b -> ResponseEntity.ok(BranchResourceFromEntityAssembler.toResourceFromEntity(b)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint to update branch image. This method is mapped to a PATCH request at the path "/api/v1/branches/{branchId}/image". It takes the branchId as a path variable and an UpdateBranchImageResource object as the request body, validates them, and uses the BranchCommandService to handle the update of the branch image. If the update is successful and a branch with the specified ID exists, the updated branch is transformed into a BranchResource object using the BranchResourceFromEntityAssembler and returned in the response body with an HTTP 200 OK status. If no branch with the given ID exists, an HTTP 404 Not Found status is returned.
     * @param branchId the unique identifier of the branch to update
     * @param resource the UpdateBranchImageResource object containing the necessary information to update the branch image
     * @return a ResponseEntity containing a BranchResource object representing the updated branch if the update was successful, or an HTTP 404 Not Found status if no branch with the given ID exists
     */
    @Operation(summary = "Update branch image")
    @PatchMapping("/{branchId}/image")
    public ResponseEntity<BranchResource> updateImage(@PathVariable String branchId,
                                                       @RequestBody UpdateBranchImageResource resource) {
        return commandService.updateImage(branchId, resource.imageUrl())
                .map(b -> ResponseEntity.ok(BranchResourceFromEntityAssembler.toResourceFromEntity(b)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint to delete a branch. This method is mapped to a DELETE request at the path "/api/v1/branches/{branchId}". It takes the branchId as a path variable, validates it, and uses the BranchCommandService to handle the deletion of the branch. If the deletion is successful, a response containing the branchId and the timestamp of deletion is returned in the response body with an HTTP 200 OK status.
     * @param branchId the unique identifier of the branch to delete
     * @return a ResponseEntity containing a map with the branchId and deletedAt timestamp if the deletion was successful
     */
    @Operation(summary = "Delete a branch")
    @DeleteMapping("/{branchId}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable String branchId) {
        commandService.delete(branchId);
        return ResponseEntity.ok(Map.of("branchId", branchId, "deletedAt", Instant.now().toString()));
    }
}
