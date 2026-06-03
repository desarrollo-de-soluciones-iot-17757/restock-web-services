package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.entities.CustomSupplyPersistenceEntity;

/**
 * Utility class responsible for converting between CustomSupply domain models and CustomSupplyPersistenceEntity objects used for MongoDB storage.
 * This class provides static methods to perform the necessary transformations while ensuring that all relevant fields are correctly mapped between the two representations. It also handles null checks to prevent potential NullPointerExceptions during the conversion process.
 */
public final class CustomSupplyPersistenceAssembler {

    // Private constructor to prevent instantiation of this utility class
    private CustomSupplyPersistenceAssembler() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Converts a CustomSupplyPersistenceEntity from MongoDB to a CustomSupply domain model.
     *
     * @param entity the CustomSupplyPersistenceEntity to convert. Must not be null.
     * @return A CustomSupply domain model representing the same data as the input entity, or null if the input is null.
     */
    public static CustomSupply toDomainFromPersistence(CustomSupplyPersistenceEntity entity) {
        if (entity == null) return null;

        var customSupply = new CustomSupply();
        customSupply.setId(entity.getId());
        customSupply.setAccountId(entity.getAccountId());
        customSupply.setName(entity.getName());
        customSupply.setSupplyId(entity.getSupplyId());
        customSupply.setSupply(SupplyPersistenceAssembler.toDomainFromPersistence(entity.getSupply()));
        customSupply.setStockRange(entity.getStockRange());
        customSupply.setUnitPrice(entity.getUnitPrice());
        customSupply.setDescription(entity.getDescription());
        customSupply.setUnitMeasurement(entity.getUnitMeasurement());
        customSupply.setPictureUrl(entity.getPictureUrl());

        return customSupply;
    }

    /**
     * Converts a CustomSupply domain model to a CustomSupplyPersistenceEntity for MongoDB storage.
     *
     * @param customSupply The CustomSupply domain model to convert. Must not be null.
     * @return A CustomSupplyPersistenceEntity representing the same data as the input domain model, or null if the input is null.
     */
    public static CustomSupplyPersistenceEntity toPersistenceFromDomain(CustomSupply customSupply) {
        if (customSupply == null) return null;

        var entity = new CustomSupplyPersistenceEntity();

        // Only set ID if it exists in the domain model, to avoid overwriting existing entities with new ones
        if (customSupply.getId() != null) {
            entity.setId(customSupply.getId());
        }
        entity.setAccountId(customSupply.getAccountId());
        entity.setName(customSupply.getName());
        entity.setSupplyId(customSupply.getSupplyId());
        entity.setSupply(SupplyPersistenceAssembler.toPersistenceFromDomain(customSupply.getSupply()));
        entity.setStockRange(customSupply.getStockRange());
        entity.setUnitPrice(customSupply.getUnitPrice());
        entity.setDescription(customSupply.getDescription());
        entity.setUnitMeasurement(customSupply.getUnitMeasurement());
        entity.setPictureUrl(customSupply.getPictureUrl());

        return entity;
    }
}
