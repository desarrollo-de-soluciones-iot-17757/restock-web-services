package com.uitopic.restock.platform.resources.interfaces.rest.transform;

import com.uitopic.restock.platform.resources.domain.model.commands.UpdateBranchStatusCommand;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.UpdateBranchStatusResource;

/**
 * Assembler class responsible for transforming incoming REST API resources into domain command objects
 * for branch status updates. This class provides a static method to convert an {@link UpdateBranchStatusResource}
 * along with the branch identifier into an {@link UpdateBranchStatusCommand} that can be processed by the
 * domain service layer.
 *
 * <p>Example usage:
 * <pre>
 *     UpdateBranchStatusResource resource = new UpdateBranchStatusResource("INACTIVE");
 *     UpdateBranchStatusCommand command = UpdateBranchCommandFromResourceAssembler.ToCommandFromResource("branch123", resource);
 * </pre>
 */
public class UpdateBranchCommandFromResourceAssembler {

    /**
     * Transforms an {@link UpdateBranchStatusResource} and a branch identifier into an {@link UpdateBranchStatusCommand}.
     * @param branchId the unique identifier of the branch
     * @param resource the resource containing the status update information
     */
    public static UpdateBranchStatusCommand ToCommandFromResource(String branchId, UpdateBranchStatusResource resource) {
        return new UpdateBranchStatusCommand(branchId, resource.status());
    }
}
