package com.uitopic.restock.platform.resources.interfaces.rest.transform;

import com.uitopic.restock.platform.resources.domain.model.commands.CreateCustomSupplyCommand;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.UpdateCustomSupplyResource;

import java.io.IOException;

/**
 * Assembler to convert an {@link UpdateCustomSupplyResource} into a
 * {@link CreateCustomSupplyCommand}.
 */
public class UpdateCustomSupplyCommandFromResourceAssembler {

    /**
     * Converts an {@link UpdateCustomSupplyResource} into a
     * {@link CreateCustomSupplyCommand}.
     *
     * <p>The {@code accountId} is always {@code null} — it cannot be changed on update.
     *
     * @param resource the incoming multipart form data
     * @return the corresponding command
     */
    public static CreateCustomSupplyCommand toCommandFromResource(UpdateCustomSupplyResource resource) {
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
                null,
                resource.supplyId(),
                resource.name(),
                resource.description(),
                resource.unitPrice(),
                resource.unitPriceCurrencyCode(),
                resource.supplyContent(),
                resource.unitMeasurement(),
                imageBytes,
                imageFileName);
    }
}
