package com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.tracking.domain.model.entities.Discrepancy;
import com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.entities.DiscrepancyPersistenceEntity;

public final class DiscrepancyPersistenceAssembler {

    private DiscrepancyPersistenceAssembler() {
        // Private constructor to prevent instantiation
    }

    public static Discrepancy toDomainFromPersistence(DiscrepancyPersistenceEntity entity) {
        return null;
    }

    public static DiscrepancyPersistenceEntity toPersistenceFromDomain(Discrepancy discrepancy) {
        return null;
    }
}
