package com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.subscriptions.domain.model.entities.Plan;
import com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.entities.PlanPersistenceEntity;

public final class PlanPersistenceAssembler {

    private PlanPersistenceAssembler() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static Plan toDomainFromPersistence(PlanPersistenceEntity entity) {
        if (entity == null) return null;
        var plan = new Plan();
        plan.setId(entity.getId());
        plan.setName(entity.getName());
        plan.setDescription(entity.getDescription());
        plan.setPrice(entity.getPrice());
        plan.setCurrency(entity.getCurrency());
        plan.setBillingInterval(entity.getBillingInterval());
        plan.setStripePriceId(entity.getStripePriceId());
        plan.setMaxRecipes(entity.getMaxRecipes());
        plan.setMaxKits(entity.getMaxKits());
        plan.setMaxDevices(entity.getMaxDevices());
        return plan;
    }

    public static PlanPersistenceEntity toPersistenceFromDomain(Plan plan) {
        if (plan == null) return null;
        var entity = new PlanPersistenceEntity();
        if (plan.getId() != null) {
            entity.setId(plan.getId());
        }
        entity.setName(plan.getName());
        entity.setDescription(plan.getDescription());
        entity.setPrice(plan.getPrice());
        entity.setCurrency(plan.getCurrency());
        entity.setBillingInterval(plan.getBillingInterval());
        entity.setStripePriceId(plan.getStripePriceId());
        entity.setMaxRecipes(plan.getMaxRecipes());
        entity.setMaxKits(plan.getMaxKits());
        entity.setMaxDevices(plan.getMaxDevices());
        return entity;
    }
}
