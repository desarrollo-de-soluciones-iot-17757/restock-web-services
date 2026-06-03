package com.uitopic.restock.platform.sales.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.sales.domain.model.aggregates.SalesOrder;
import com.uitopic.restock.platform.sales.infrastructure.persistence.mongodb.entities.SalesOrderPersistenceEntity;

public final class SalesOrderPersistenceAssembler {

    private SalesOrderPersistenceAssembler() {
        // Private constructor to prevent instantiation
    }

    public static SalesOrder toDomainFromPersistence(SalesOrderPersistenceEntity entity) {
        return null;
    }

    public static SalesOrderPersistenceEntity toPersistenceFromDomain(SalesOrder salesOrder) {
        return null;
    }
}
