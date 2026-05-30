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
 * @param imageUrl       (optional) URL of an image representing the branch
 * @param description    (optional) a description of the branch
 */
public record CreateBranchCommand(
        String accountId,
        String name,
        String address,
        String city,
        String regionOrState,
        String country,
        String imageUrl,
        String description
) {}
