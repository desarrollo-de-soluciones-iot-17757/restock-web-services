package com.uitopic.restock.platform.profiles.interfaces.rest.transform;

import com.uitopic.restock.platform.profiles.domain.model.commands.CreateBusinessCommand;
import com.uitopic.restock.platform.profiles.interfaces.rest.resources.CreateBusinessResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public final class CreateBusinessCommandFromResourceAssembler {

    private CreateBusinessCommandFromResourceAssembler() {}

    public static CreateBusinessCommand toCommandFromResource(CreateBusinessResource resource) {
        return new CreateBusinessCommand(
                resource.userId(),
                resource.ruc(),
                getBytes(resource.image()),
                getFileName(resource.image()),
                resource.companyName(),
                resource.mainLocation()
        );
    }

    private static byte[] getBytes(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read business image", e);
        }
    }

    private static String getFileName(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;
        return file.getOriginalFilename();
    }
}
