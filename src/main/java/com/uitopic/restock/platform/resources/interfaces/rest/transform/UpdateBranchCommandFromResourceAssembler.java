package com.uitopic.restock.platform.resources.interfaces.rest.transform;

import com.uitopic.restock.platform.resources.domain.model.commands.UpdateBranchCommand;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.UpdateBranchResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Assembler to convert UpdateBranchResource into UpdateBranchCommand.
 */
public class UpdateBranchCommandFromResourceAssembler {

    public static UpdateBranchCommand toCommandFromResource(String branchId, UpdateBranchResource resource) {
        return new UpdateBranchCommand(
                branchId,
                resource.name(),
                resource.description(),
                resource.address(),
                resource.city(),
                resource.regionOrState(),
                resource.country(),
                getBytes(resource.image()),
                getFileName(resource.image())
        );
    }

    private static byte[] getBytes(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read branch image", e);
        }
    }

    private static String getFileName(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        return file.getOriginalFilename();
    }
}