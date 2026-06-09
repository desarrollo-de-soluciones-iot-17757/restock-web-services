package com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.tracking.domain.model.aggregates.StockComparisonTask;
import com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.entities.StockComparisonTaskPersistenceEntity;

public final class StockComparisonTaskPersistenceAssembler {

    private StockComparisonTaskPersistenceAssembler() {
        // Private constructor to prevent instantiation
    }

    public static StockComparisonTask toDomainFromPersistence(StockComparisonTaskPersistenceEntity entity) {
        return null;
    }

    public static StockComparisonTaskPersistenceEntity toPersistenceFromDomain(StockComparisonTask domain) {
        return null;
    }
}
