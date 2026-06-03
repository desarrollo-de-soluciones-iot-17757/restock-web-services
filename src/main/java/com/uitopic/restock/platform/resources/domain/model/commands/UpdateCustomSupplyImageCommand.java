package com.uitopic.restock.platform.resources.domain.model.commands;

/**
 * Command to upload or replace the image of an existing custom supply.
 *
 * @param id            the unique identifier of the custom supply
 * @param image         raw bytes of the new image file
 * @param imageFileName original file name (used to infer MIME type / Cloudinary naming)
 */
public record UpdateCustomSupplyImageCommand(
        String id,
        byte[] image,
        String imageFileName
) {
}
