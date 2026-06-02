package com.uitopic.restock.platform.resources.domain.model.commands;

/**
 * Command object for updating the status of a branch. This command is used to change the operational
 * status of a branch, such as activating or deactivating it. The command contains the identifier
 * of the branch to be updated and the new status value.
 *
 * <p>Example usage:
 * <pre>
 *     UpdateBranchStatusCommand command = new UpdateBranchStatusCommand("branch123", "INACTIVE");
 *     branchCommandService.handle(command);
 * </pre>
 *
 * @param branchId the unique identifier of the branch to update
 * @param status   the new status to set for the branch (e.g., "ACTIVE" or "INACTIVE")
 */
public record UpdateBranchStatusCommand(
        String branchId,
        String status
) {
}
