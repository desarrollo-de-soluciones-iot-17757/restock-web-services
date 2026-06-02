package com.uitopic.restock.platform.resources.interfaces.rest.resources;

/**
 * Resource class representing the payload for updating a branch's status via the REST API.
 * This class is used to deserialize incoming JSON requests that contain the new status value
 * for a branch. It corresponds to the {@link com.uitopic.restock.platform.resources.domain.model.commands.UpdateBranchStatusCommand}
 * command object in the domain layer, which is used to perform the actual status update operation.
 *
 * <p>Example JSON payload:
 * <pre>
 *     {
 *         "status": "INACTIVE"
 *     }
 * </pre>
 *
 * @param status the new status to set for the branch (e.g., "ACTIVE" or "INACTIVE")
 */
public record UpdateBranchStatusResource(
        String status
) {
}
