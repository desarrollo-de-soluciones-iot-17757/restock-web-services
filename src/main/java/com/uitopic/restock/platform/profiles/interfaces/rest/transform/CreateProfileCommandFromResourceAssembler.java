package com.uitopic.restock.platform.profiles.interfaces.rest.transform;

import com.uitopic.restock.platform.profiles.domain.model.commands.CreateProfileCommand;
import com.uitopic.restock.platform.profiles.interfaces.rest.resources.CreateProfileResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public final class CreateProfileCommandFromResourceAssembler {

    private CreateProfileCommandFromResourceAssembler() {}

    public static CreateProfileCommand toCommandFromResource(CreateProfileResource resource) {
        return new CreateProfileCommand(
                resource.accountId(),
                resource.userId(),
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
