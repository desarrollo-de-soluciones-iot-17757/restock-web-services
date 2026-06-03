package com.uitopic.restock.platform.profiles.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.profiles.domain.model.aggregates.Business;
import com.uitopic.restock.platform.profiles.infrastructure.persistence.mongodb.entities.BusinessPersistenceEntity;

public final class BusinessPersistenceAssembler {

    private BusinessPersistenceAssembler() {
        // Private constructor to prevent instantiation
    }

    public static Business toDomainFromPersistence(BusinessPersistenceEntity entity) {
        return null;
    }

    public static BusinessPersistenceEntity toPersistenceFromDomain(Business business) {
        return null;
    }
}
