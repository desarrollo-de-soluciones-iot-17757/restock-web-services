package com.uitopic.restock.platform.resources.domain.model.commands;

/**
 * Command object for updating branch information.
 *
 * @param branchId       the ID of the branch to update
 * @param name           the new name of the branch
 * @param address        the new street address of the branch
 * @param city           the new city where the branch is located
 * @param regionOrState  the new region or state where the branch is located
 * @param country        the new country where the branch is located
 * @param description    (optional) a new description of the branch
 */
public record UpdateBranchInfoCommand(
        String branchId,
        String name,
        String address,
        String city,
        String regionOrState,
        String country,
        String description
) {}
