package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

/**
 * Request resource for updating the image of a custom supply within the resources bounded context.
 *
 * @param image the new image file for the custom supply
 */
@Schema(description = "Request resource for updating custom supply image")
public record UpdateCustomSupplyImageResource(
        @Schema(description = "Image file") MultipartFile image
) {
    /**
     * Returns {@code true} if a non-empty image file was provided.
     */
    public boolean hasImage() {
        return this.image != null
                && !this.image.isEmpty()
                && this.image.getOriginalFilename() != null
                && !this.image.getOriginalFilename().isBlank();
    }
}
