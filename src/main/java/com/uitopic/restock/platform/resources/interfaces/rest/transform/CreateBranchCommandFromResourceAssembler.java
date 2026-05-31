package com.uitopic.restock.platform.resources.interfaces.rest.transform;

import com.uitopic.restock.platform.resources.domain.model.commands.CreateBranchCommand;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.CreateBranchResource;

/**
 * Assembler class responsible for converting CreateBranchResource objects into CreateBranchCommand objects. This class provides a static method that takes a CreateBranchResource as input and returns a corresponding CreateBranchCommand, which can then be used in the application layer to handle the creation of a new branch.
 */
public class CreateBranchCommandFromResourceAssembler {

    /**
     * Converts a CreateBranchResource object into a CreateBranchCommand object.
     *
     * @param resource createBranchResource to be converted
     * @return a CreateBranchCommand containing the data from the resource
     * @throws RuntimeException if there is an error during conversion, such as issues with reading the image file
     */
    public static CreateBranchCommand ToCommandFromResource(CreateBranchResource resource, String accountId) {
        try {

            if (!resource.hasNewPhoto()) {
                return new CreateBranchCommand(accountId, resource.name(), resource.address(), resource.city(), resource.stateOrRegion(), resource.country(), resource.description(), null, null);
            }

            return new CreateBranchCommand(
                    accountId,
                    resource.name(),
                    resource.address(),
                    resource.city(),
                    resource.stateOrRegion(),
                    resource.country(),
                    resource.description(),
                    resource.image().getBytes(),
                    resource.image().getOriginalFilename()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert CreateBranchResource to CreateBranchCommand", e);
        }
    }
}
