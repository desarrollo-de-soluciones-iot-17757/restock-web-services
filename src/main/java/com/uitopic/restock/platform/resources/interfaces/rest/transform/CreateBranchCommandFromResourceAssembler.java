package com.uitopic.restock.platform.resources.interfaces.rest.transform;

import com.uitopic.restock.platform.resources.domain.model.commands.CreateBranchCommand;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.CreateBranchResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Assembler to convert CreateBranchResource into CreateBranchCommand.
 */
public class CreateBranchCommandFromResourceAssembler {

    public static CreateBranchCommand toCommandFromResource(String accountId, CreateBranchResource resource) {
        return new CreateBranchCommand(
                accountId,
                resource.name(),
                resource.address(),
                resource.city(),
                resource.regionOrState(),
                resource.country(),
                resource.description(),
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