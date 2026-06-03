package com.uitopic.restock.platform.resources.domain.model.commands;

/**
 * Command to create a new branch.
 */
public record CreateBranchCommand(
        String accountId,
        String name,
        String address,
        String city,
        String regionOrState,
        String country,
        String description,
        byte[] image,
        String photoFileName
) {
    public CreateBranchCommand {
        validateText(accountId, "Account ID");
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