package com.uitopic.restock.platform.resources.interfaces.rest.transform;

import com.uitopic.restock.platform.resources.domain.model.commands.UpdateBranchImageCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.UpdateBranchInfoCommand;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.UpdateBranchInfoResource;

/**
 * Assembler to convert {@link com.uitopic.restock.platform.resources.interfaces.rest.resources.UpdateBranchInfoResource}
 * to {@link com.uitopic.restock.platform.resources.domain.model.commands.UpdateBranchInfoCommand}
 */
public class UpdateBranchInfoCommandFromResourceAssembler {

    /** Converts an UpdateBranchInfoResource to an UpdateBranchInfoCommand.
     *
     * @param resource the UpdateBranchInfoResource containing the branch information data
     * @param branchId the ID of the branch to be updated
     * @return an UpdateBranchInfoCommand with the branch information data and branch ID
     */
    public static UpdateBranchInfoCommand ToCommandFromResource(UpdateBranchInfoResource resource, String branchId) {
       try {
           
           if (!resource.hasImage()) {
               return new UpdateBranchInfoCommand(
                       branchId,
                       resource.name(),
                       resource.address(),
                       resource.city(),
                       resource.regionOrState(),
                       resource.country(),
                       resource.description(),
                       null,
                       null
               );
           }

           return new UpdateBranchInfoCommand(
                   branchId,
                   resource.name(),
                   resource.address(),
                   resource.city(),
                   resource.regionOrState(),
                   resource.country(),
                   resource.description(),
                   resource.image().getBytes(),
                   resource.image().getOriginalFilename()
           );

       } catch (Exception e) {
           throw new RuntimeException("Failed to convert UpdateBranchInfoResource to UpdateBranchInfoCommand", e);
       }
    }
}
