package com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.tracking.domain.model.entities.Discrepancy;
import com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.entities.DiscrepancyPersistenceEntity;

/**
 * Assembler class responsible for converting between Discrepancy domain entities and DiscrepancyPersistenceEntity objects used for MongoDB storage. This class provides static methods to facilitate the transformation of data between the domain model and the persistence layer, ensuring that the data is correctly mapped when saving to or retrieving from MongoDB.
 */
public final class DiscrepancyPersistenceAssembler {

    private DiscrepancyPersistenceAssembler() {
        // Private constructor to prevent instantiation
    }

    /**
     * Converts a DiscrepancyPersistenceEntity from MongoDB to a Discrepancy domain entity. If the persistence entity is null, it returns null.
     *
     * @param entity the DiscrepancyPersistenceEntity retrieved from MongoDB, which may be null
     * @return a Discrepancy domain entity containing the data from the persistence entity, or null if the input entity is null
     */
    public static Discrepancy toDomainFromPersistence(DiscrepancyPersistenceEntity entity) {
        if (entity == null) return null;

        var discrepancy = new Discrepancy();
        discrepancy.setId(entity.getId());
        discrepancy.setRiskLevel(entity.getRiskLevel());
        discrepancy.setQuantityDifference(entity.getQuantityDifference());
        discrepancy.setStatus(entity.getStatus());
        discrepancy.setDeviceId(entity.getDeviceId());

        return discrepancy;
    }

    /**
     * Converts a Discrepancy domain entity to a DiscrepancyPersistenceEntity for MongoDB storage. If the domain entity has an ID, it will be set in the persistence entity; otherwise, MongoDB will generate a new ID upon saving.
     *
     * @param discrepancy the Discrepancy domain entity to be converted to a persistence entity for MongoDB storage
     * @return a DiscrepancyPersistenceEntity containing the data from the Discrepancy domain entity, ready for storage in MongoDB
     */
    public static DiscrepancyPersistenceEntity toPersistenceFromDomain(Discrepancy discrepancy) {
        if (discrepancy == null) return null;

        var entity = new DiscrepancyPersistenceEntity();

        // Only set the ID if it exists in the domain model, otherwise let MongoDB generate it
        if (discrepancy.getId() != null) {
            entity.setId(discrepancy.getId());
        }
        entity.setRiskLevel(discrepancy.getRiskLevel());
        entity.setQuantityDifference(discrepancy.getQuantityDifference());
        entity.setStatus(discrepancy.getStatus());
        entity.setDeviceId(discrepancy.getDeviceId());

        return entity;
    }
}
