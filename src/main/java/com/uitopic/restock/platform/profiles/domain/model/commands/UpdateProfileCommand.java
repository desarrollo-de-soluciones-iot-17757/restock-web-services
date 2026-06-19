package com.uitopic.restock.platform.profiles.domain.model.commands;

public record UpdateProfileCommand(
        String id,
        String name,
        String lastName,
        String phoneNumber,
        byte[] image,
        String photoFileName,
        String gender,
        String birthDate
) {
    public boolean hasImage() {
        return image != null && image.length > 0
                && photoFileName != null && !photoFileName.isBlank();
    }
}
