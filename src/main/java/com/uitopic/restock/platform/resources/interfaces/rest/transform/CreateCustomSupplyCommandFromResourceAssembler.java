package com.uitopic.restock.platform.resources.interfaces.rest.transform;

import com.uitopic.restock.platform.resources.domain.model.commands.CreateCustomSupplyCommand;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.CreateCustomSupplyResource;

import java.io.IOException;

/**
 * Assembler to convert a {@link CreateCustomSupplyResource} into a
 * {@link CreateCustomSupplyCommand}.
 */
public class CreateCustomSupplyCommandFromResourceAssembler {

    /**
     * Converts a {@link CreateCustomSupplyResource} into a
     * {@link CreateCustomSupplyCommand}.
     *
     * @param resource the incoming multipart form data
     * @return the corresponding command
     */
    public static CreateCustomSupplyCommand toCommandFromResource(CreateCustomSupplyResource resource) {
        byte[] imageBytes = null;
        String imageFileName = null;

        if (resource.hasImage()) {
            try {
                imageBytes = resource.image().getBytes();
                imageFileName = resource.image().getOriginalFilename();
            } catch (IOException e) {
                throw new IllegalArgumentException("Error reading image file: " + e.getMessage());
            }
        }

        return new CreateCustomSupplyCommand(
                resource.accountId(),
                resource.supplyId(),
                resource.name(),
                resource.description(),
                resource.unitPrice(),
                resource.unitPriceCurrencyCode(),
                resource.supplyContent(),
                resource.unitMeasurement(),
                imageBytes,
                imageFileName
        );
    }
}
