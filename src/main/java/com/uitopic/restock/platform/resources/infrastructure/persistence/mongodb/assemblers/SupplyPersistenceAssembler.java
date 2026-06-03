package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.resources.domain.model.entities.Supply;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.entities.SupplyPersistenceEntity;

/**
 * Utility class responsible for converting between Supply domain models and SupplyPersistenceEntity objects used for MongoDB storage. This assembler provides methods to translate data between the two representations, ensuring that the application can seamlessly interact with the database while maintaining a clear separation of concerns between the domain model and persistence layer.
 */
public final class SupplyPersistenceAssembler {

    // Private constructor to prevent instantiation of this utility class
    private SupplyPersistenceAssembler() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Converts a SupplyPersistenceEntity from MongoDB to a Supply domain model.
     *
     * @param entity the SupplyPersistenceEntity to convert. Must not be null.
     * @return A Supply domain model representing the same data as the input entity, or null if the input is null.
     */
    public static Supply toDomainFromPersistence(SupplyPersistenceEntity entity) {
        if (entity == null) return null;

        var supply = new Supply();
        supply.setId(entity.getId());
        supply.setName(entity.getName());
        supply.setDescription(entity.getDescription());
        supply.setCategory(entity.getCategory());
        supply.setIsPerishable(entity.getIsPerishable());

        return supply;
    }

    /**
     * Converts a Supply domain model to a SupplyPersistenceEntity for MongoDB storage.
     *
     * @param supply The Supply domain model to convert. Must not be null.
     * @return A SupplyPersistenceEntity representing the same data as the input domain model, or null if the input is null.
     */
    public static SupplyPersistenceEntity toPersistenceFromDomain(Supply supply) {
        if (supply == null) return null;

        var entity = new SupplyPersistenceEntity();

        // Only set ID if it exists in the domain model, to avoid overwriting existing entities with new ones
        if (supply.getId() != null) {
            entity.setId(supply.getId());
        }
        entity.setName(supply.getName());
        entity.setDescription(supply.getDescription());
        entity.setCategory(supply.getCategory());
        entity.setIsPerishable(supply.getIsPerishable());

        return entity;
    }
}
