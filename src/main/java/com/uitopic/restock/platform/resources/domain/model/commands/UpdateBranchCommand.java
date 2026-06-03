package com.uitopic.restock.platform.resources.domain.model.commands;

/**
 * Command to update branch information.
 */
public record UpdateBranchCommand(
        String branchId,
        String name,
        String description,
        String address,
        String city,
        String regionOrState,
        String country,
        byte[] image,
        String photoFileName
) {
    public UpdateBranchCommand {
        validateText(branchId, "Branch ID");
        validateText(name, "Branch name");
        validateText(address, "Branch address");
        validateText(city, "Branch city");
        validateText(country, "Branch country");
    }

    /**
     * Indicates whether the command contains a new photo.
     *
     * @return true if image data and file name are present
     */
    public boolean hasNewPhoto() {
        return image != null && image.length > 0
                && photoFileName != null && !photoFileName.isBlank();
    }

    private static void validateText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
        }
    }
}