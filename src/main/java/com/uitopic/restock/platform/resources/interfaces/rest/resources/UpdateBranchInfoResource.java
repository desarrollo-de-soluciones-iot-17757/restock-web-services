package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

/**
 * Request resource for updating branch information within the resources bounded context.
 *
 * @param name           the new name of the branch
 * @param address        the new street address of the branch
 * @param city           the new city where the branch is located
 * @param regionOrState  the new region or state where the branch is located
 * @param country        the new country where the branch is located
 * @param description    (optional) a new description of the branch
 * @param image          (optional) a new image file for the branch
 */
@Schema(description = "Request resource for updating branch info")
public record UpdateBranchInfoResource(
        @Schema(description = "Branch name") String name,
        @Schema(description = "Branch address") String address,
        @Schema(description = "City") String city,
        @Schema(description = "State or Region") String regionOrState,
        @Schema(description = "Country") String country,
        @Schema(description = "Branch description") String description,
        @Schema(description = "New photo file name") MultipartFile image,
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
