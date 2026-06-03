package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Batch;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.entities.BatchPersistenceEntity;

/**
 * Utility class responsible for converting between Batch domain models and BatchPersistenceEntity objects used for MongoDB storage.
 */
public final class BatchPersistenceAssembler {

    // This class is a utility class and should not be instantiated.
    private BatchPersistenceAssembler() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Converts a BatchPersistenceEntity from MongoDB to a Batch domain model.
     *
     * @param entity The BatchPersistenceEntity to convert. Must not be null.
     * @return A Batch domain model representing the given entity, or null if the input is null.
     */
    public static Batch toDomainFromPersistence(BatchPersistenceEntity entity) {
        if (entity == null) return null;

        var batch = new Batch();
        batch.setId(entity.getId());
        batch.setCode(entity.getCode());
        batch.setCurrentStock(entity.getCurrentStock());
        batch.setCustomSupplyId(entity.getCustomSupplyId());
        batch.setBranchId(entity.getBranchId());
        batch.setAccountId(entity.getAccountId());
        batch.setExpirationDate(entity.getExpirationDate());
        batch.setEntryDate(entity.getEntryDate());

        return batch;
    }

    /**
     * Converts a Batch domain model to a BatchPersistenceEntity for MongoDB storage.
     *
     * @param batch The Batch domain model to convert. Must not be null.
     * @return A BatchPersistenceEntity representing the given Batch, or null if the input is null.
     */
    public static BatchPersistenceEntity toPersistenceFromDomain(Batch batch) {
        if (batch == null) return null;

        var entity = new BatchPersistenceEntity();

        // Only set the ID if it exists in the domain model, otherwise let MongoDB generate it.
        if (batch.getId() != null) {
            entity.setId(batch.getId());
        }
        entity.setCode(batch.getCode());
        entity.setCurrentStock(batch.getCurrentStock());
        entity.setCustomSupplyId(batch.getCustomSupplyId());
        entity.setBranchId(batch.getBranchId());
        entity.setAccountId(batch.getAccountId());
        entity.setExpirationDate(batch.getExpirationDate());
        entity.setEntryDate(batch.getEntryDate());

        return entity;
    }
}
