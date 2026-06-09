package com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.subscriptions.domain.model.entities.Plan;
import com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.entities.PlanPersistenceEntity;

public final class PlanPersistenceAssembler {

    private PlanPersistenceAssembler() {
        // Private constructor to prevent instantiation
    }

    public static Plan toDomainFromPersistence(PlanPersistenceEntity entity) {
        return null;
    }

    public static PlanPersistenceEntity toPersistenceFromDomain(Plan plan) {
        return null;
    }
}
