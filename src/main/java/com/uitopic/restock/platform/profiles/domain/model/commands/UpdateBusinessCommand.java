package com.uitopic.restock.platform.profiles.domain.model.commands;

public record UpdateBusinessCommand(
        String id,
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
