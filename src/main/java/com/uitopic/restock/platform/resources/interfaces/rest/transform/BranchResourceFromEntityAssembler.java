package com.uitopic.restock.platform.resources.interfaces.rest.transform;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Branch;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.BranchResource;

/**
 * Assembler class to convert Branch entities to BranchResource DTOs for REST API responses.
 */
public class BranchResourceFromEntityAssembler {

    /**
     * Converts a Branch entity to a BranchResource DTO.
     *
     * @param entity the Branch entity to convert
     * @return a BranchResource DTO containing the data from the Branch entity
     */
    public static BranchResource toResourceFromEntity(Branch entity) {
        return new BranchResource(
                entity.getId(),
                entity.getAccountId().getAccountId(),
                entity.getName(),
                entity.getLocation().address(),
                entity.getLocation().city(),
                entity.getLocation().regionOrState(),
                entity.getLocation().country(),
                entity.getImageUrl() != null ? entity.getImageUrl().getUrl() : null,
                entity.getStatus().name().toLowerCase(),
                entity.getDescription(),
                entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null
        );
    }
}
