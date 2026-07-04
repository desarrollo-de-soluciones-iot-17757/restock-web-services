package com.uitopic.restock.platform.subscriptions.interfaces.rest.transform;

import com.uitopic.restock.platform.subscriptions.domain.model.entities.Plan;
import com.uitopic.restock.platform.subscriptions.interfaces.rest.resources.PlanResource;

public final class PlanResourceFromEntityAssembler {

    private PlanResourceFromEntityAssembler() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static PlanResource toResourceFromEntity(Plan plan) {
        if (plan == null) return null;
        return new PlanResource(
                plan.getId(),
                plan.getName(),
                plan.getDescription(),
                plan.getPrice(),
                plan.getCurrency(),
                plan.getBillingInterval(),
                plan.getStripePriceId(),
                plan.getMaxRecipes(),
                plan.getMaxKits(),
                plan.getMaxDevices()
        );
    }
}
