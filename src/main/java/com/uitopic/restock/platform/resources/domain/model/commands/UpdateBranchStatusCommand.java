package com.uitopic.restock.platform.resources.domain.model.commands;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.BranchStates;

/**
 * Command to update branch status.
 */
public record UpdateBranchStatusCommand(
        String branchId,
        BranchStates status
) {
    public UpdateBranchStatusCommand {
        if (branchId == null || branchId.isBlank()) {
            throw new IllegalArgumentException("Branch ID cannot be null or blank");
        }

        if (status == null) {
            throw new IllegalArgumentException("Branch status cannot be null");
        }
    }
}