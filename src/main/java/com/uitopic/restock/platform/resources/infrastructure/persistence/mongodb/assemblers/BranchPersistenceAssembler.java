package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Branch;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.entities.BranchPersistenceEntity;

/**
 * Utility class for converting between Branch domain models and BranchPersistenceEntity objects used for MongoDB storage.
 */
public final class BranchPersistenceAssembler {

    // This class is a utility class and should not be instantiated.
    private BranchPersistenceAssembler() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Converts a BranchPersistenceEntity from MongoDB to a Branch domain model.
     *
     * @param entity The BranchPersistenceEntity to convert.
     * @return A Branch domain model representing the same data as the input entity, or null if the input entity is null.
     */
    public static Branch toDomainFromPersistence(BranchPersistenceEntity entity) {
        if (entity == null) return null;

        var branch = new Branch();
        branch.setId(entity.getId());
        branch.setName(entity.getName());
        branch.setDescription(entity.getDescription());
        branch.setImageUrl(entity.getImageUrl());
        branch.setLocation(entity.getLocation());
        branch.setStatus(entity.getStatus());
        branch.setAccountId(entity.getAccountId());

        return branch;
    }

    /**
     * Converts a Branch domain model to a BranchPersistenceEntity for MongoDB storage.
     *
     * @param branch The Branch domain model to convert.
     * @return A BranchPersistenceEntity representing the same data as the input Branch, suitable for persistence in MongoDB.
     */
    public static BranchPersistenceEntity toPersistenceFromDomain(Branch branch) {
        if (branch == null) return null;

        var entity = new BranchPersistenceEntity();

        // Only set the ID if it exists in the domain model, otherwise let MongoDB generate it.
        if (branch.getId() != null) {
            entity.setId(branch.getId());
        }
        entity.setName(branch.getName());
        entity.setDescription(branch.getDescription());
        entity.setImageUrl(branch.getImageUrl());
        entity.setLocation(branch.getLocation());
        entity.setStatus(branch.getStatus());
        entity.setAccountId(branch.getAccountId());

        return entity;
    }
}
