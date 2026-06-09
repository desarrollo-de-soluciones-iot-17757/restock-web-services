package com.uitopic.restock.platform.sales.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.sales.domain.model.entities.SalesOrderItem;
import com.uitopic.restock.platform.sales.infrastructure.persistence.mongodb.entities.SalesOrderItemPersistenceEntity;

public final class SalesOrderItemPersistenceAssembler {

    private SalesOrderItemPersistenceAssembler() {
        // Private constructor to prevent instantiation
    }

    public static SalesOrderItem toDomainFromPersistence(SalesOrderItemPersistenceEntity entity) {
        return null;
    }

    public static SalesOrderItemPersistenceEntity toPersistenceFromDomain(SalesOrderItem domain) {
        return null;
    }
}
