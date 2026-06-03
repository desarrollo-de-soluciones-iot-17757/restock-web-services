package com.uitopic.restock.platform.resources.interfaces.rest.transform;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Branch;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.BranchResource;

/**
 * Assembler to convert Branch into BranchResource.
 */
public class BranchResourceFromEntityAssembler {

    public static BranchResource toResourceFromEntity(Branch entity) {
        var location = entity.getLocation();
        var image = entity.getImageUrl();

        return new BranchResource(
                entity.getId(),
                entity.getAccountId().getAccountId(),
                entity.getName(),
                entity.getDescription(),
                location != null ? location.address() : null,
                location != null ? location.city() : null,
                location != null ? location.regionOrState() : null,
                location != null ? location.country() : null,
                entity.getStatus() != null ? entity.getStatus().name() : null,
                image != null ? image.url() : null,
                image != null ? image.publicId() : null,
                entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null
        );
    }
}