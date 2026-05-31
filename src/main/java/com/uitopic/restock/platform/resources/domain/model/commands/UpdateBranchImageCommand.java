package com.uitopic.restock.platform.resources.domain.model.commands;

/**
 * Command object for updating the image of an existing branch within the resources bounded context.
 *
 * @param branchId           the ID of the branch to be updated
 * @param image              a byte array representing the new image of the branch
 * @param photoFileName      the name of the new photo file
 * @param shouldRemoveImage  whether to remove the current image
 */
public record UpdateBranchImageCommand(
        String branchId,
        byte[] image,
        String photoFileName,
        boolean shouldRemoveImage
) {

    /** Checks if the command contains a valid image. This method returns true if the image and photoFileName are not null, indicating that there is a new image to update.
     *
     * @return true if both image and photoFileName are present, false otherwise
     */
    public boolean hasNewPhoto() {
        return image != null && photoFileName != null;
    }
}
