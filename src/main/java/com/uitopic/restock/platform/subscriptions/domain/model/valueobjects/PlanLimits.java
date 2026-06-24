package com.uitopic.restock.platform.subscriptions.domain.model.valueobjects;

public record PlanLimits(
        int maxBranches,
        int maxDevices,
        int maxSupplies,
        int maxProducts
) {
    public PlanLimits {
        if (maxBranches < 1 || maxDevices < 1 || maxSupplies < 1 || maxProducts < 1) {
            throw new IllegalArgumentException("Plan limits must be greater than zero");
        }
    }
}
