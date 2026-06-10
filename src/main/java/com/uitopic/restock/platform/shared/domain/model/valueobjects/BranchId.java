package com.uitopic.restock.platform.shared.domain.model.valueobjects;

/**
 * Value object representing a branch ID in the inventory management system.
 * This class is immutable and ensures that the branch ID is valid (not null or blank).
 */
public record BranchId(
        String branchId
) {

    public BranchId {
        if (branchId == null || branchId.isBlank()) {
            throw new IllegalArgumentException("Branch ID cannot be null or blank");
        }
    }

    /**
     * Returns the branch ID.
     *
     * @return the branch ID
     */
    public String getBranchId() {
        return branchId;
    }
}
