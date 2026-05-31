package com.uitopic.restock.platform.resources.domain.model.commands;

/**
 * Command object for updating the image of an existing branch.
 *
 * @param branchId      the ID of the branch to be updated
 * @param image         a byte array representing the new image of the branch
 * @param photoFileName the name of the new photo file
 */
public record UpdateBranchImageCommand(
        String branchId,
        byte[] image,
        String photoFileName
) {

    /** Checks if the command contains a valid image. This method returns true if the image is null or empty, indicating that there is no new image to update. Otherwise, it returns false, indicating that there is a new image to be processed.
     *
     * @return true if the image is null or empty, false otherwise
     */
    public boolean hasNewPhoto() {
        return image != null && photoFileName != null;
    }
}
