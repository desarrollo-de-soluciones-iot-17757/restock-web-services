package com.uitopic.restock.platform.resources.domain.model.commands;

/**
 * Command object for creating a new branch.
 *
 * @param accountId      the ID of the account to which the branch belongs
 * @param name           the name of the branch
 * @param address        the street address of the branch
 * @param city           the city where the branch is located
 * @param regionOrState  the region or state where the branch is located
 * @param country        the country where the branch is located
 * @param description    (optional) a description of the branch
 * @param image          (optional) a byte array representing the image of the branch
 * @param photoFileName (optional) the name of the photo file
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
    public boolean hasNewPhoto() {
        return image != null && photoFileName != null;
    }
}
