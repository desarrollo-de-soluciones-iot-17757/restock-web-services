package com.uitopic.restock.platform.resources.interfaces.rest.transform;

import com.uitopic.restock.platform.resources.domain.model.commands.UpdateBranchImageCommand;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.UpdateBranchImageResource;

/**
 * Assembler to convert {@link com.uitopic.restock.platform.resources.interfaces.rest.resources.UpdateBranchImageResource}
 * to {@link com.uitopic.restock.platform.resources.domain.model.commands.UpdateBranchImageCommand}
 * within the resources bounded context.
 */
public class UpdateBranchImageCommandFromResourceAssembler {

    /** Converts an UpdateBranchImageResource to an UpdateBranchImageCommand.
     *
     * @param resource the UpdateBranchImageResource containing the image data
     * @param branchId the ID of the branch to be updated
     * @return an UpdateBranchImageCommand with the image data and branch ID
     */
    public static UpdateBranchImageCommand ToCommandFromResource(UpdateBranchImageResource resource, String branchId) {
        try {
            if (!resource.hasImage()) {
                return new UpdateBranchImageCommand(branchId, null, null);
            }

            return new UpdateBranchImageCommand(
                    branchId,
                    resource.image().getBytes(),
                    resource.image().getOriginalFilename()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert UpdateBranchImageResource to UpdateBranchImageCommand", e);
        }
    }
}
