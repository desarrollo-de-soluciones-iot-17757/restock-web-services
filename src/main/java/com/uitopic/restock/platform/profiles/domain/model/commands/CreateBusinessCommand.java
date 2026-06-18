package com.uitopic.restock.platform.profiles.domain.model.commands;

public record CreateBusinessCommand(
        String userId,
        String ruc,
        byte[] image,
        String photoFileName,
        String companyName,
        String mainLocation
) {
    public boolean hasImage() {
        return image != null && image.length > 0
                && photoFileName != null && !photoFileName.isBlank();
    }
}
