package com.uitopic.restock.platform.resources.domain.model.commands;

/**
 * Command to delete or deactivate a branch.
 */
public record DeleteBranchCommand(
        String branchId
) {
    public DeleteBranchCommand {
        if (branchId == null || branchId.isBlank()) {
            throw new IllegalArgumentException("Branch ID cannot be null or blank");
        }
    }
}