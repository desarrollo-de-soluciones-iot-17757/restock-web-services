package com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.tracking.domain.model.aggregates.ConciliationTask;
import com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.entities.ConciliationTaskPersistenceEntity;

public final class ConciliationTaskPersistenceAssembler {

    private ConciliationTaskPersistenceAssembler() {
        // Private constructor to prevent instantiation
    }

    public static ConciliationTask toDomainFromPersistence(ConciliationTaskPersistenceEntity entity) {
        return null;
    }

    public static ConciliationTaskPersistenceEntity toPersistenceFromDomain(ConciliationTask domain) {
        return null;
    }
}
