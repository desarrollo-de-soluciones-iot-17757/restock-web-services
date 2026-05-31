package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

/**
 * Request resource for updating branch image within the resources bounded context.
 *
 * @param image the new image file for the branch
 */
@Schema(description = "Request resource for updating branch image")
public record UpdateBranchImageResource(
        @Schema(description = "Image") MultipartFile image,
        @Schema(description = "Whether to remove the current image", example = "false") Boolean removeImage
) {
    /** Helper method to check if an image file is provided in the request.
     *
     * @return true if an image file is provided and not empty, false otherwise
     */
    public boolean hasImage() {
        return this.image != null
                && !this.image.isEmpty()
                && this.image.getOriginalFilename() != null
                && !this.image.getOriginalFilename().isBlank();
    }

    /** Helper method to check if the image should be removed.
     *
     * @return true if removeImage flag is explicitly set to true, false otherwise
     */
    public boolean shouldRemoveImage() {
        return this.removeImage != null && this.removeImage;
    }
}
