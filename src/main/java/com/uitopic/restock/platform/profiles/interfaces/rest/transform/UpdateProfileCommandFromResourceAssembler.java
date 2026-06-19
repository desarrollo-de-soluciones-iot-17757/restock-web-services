package com.uitopic.restock.platform.profiles.interfaces.rest.transform;

import com.uitopic.restock.platform.profiles.domain.model.commands.UpdateProfileCommand;
import com.uitopic.restock.platform.profiles.interfaces.rest.resources.UpdateProfileResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public final class UpdateProfileCommandFromResourceAssembler {

    private UpdateProfileCommandFromResourceAssembler() {}

    public static UpdateProfileCommand toCommandFromResource(String id, UpdateProfileResource resource) {
        return new UpdateProfileCommand(
                id,
                resource.name(),
                resource.lastName(),
                resource.phoneNumber(),
                getBytes(resource.image()),
                getFileName(resource.image()),
                resource.gender(),
                resource.birthDate()
        );
    }

    private static byte[] getBytes(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read profile image", e);
        }
    }

    private static String getFileName(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;
        return file.getOriginalFilename();
    }
}
