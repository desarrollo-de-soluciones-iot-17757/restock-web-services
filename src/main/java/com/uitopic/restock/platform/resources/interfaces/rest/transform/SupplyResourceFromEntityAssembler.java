package com.uitopic.restock.platform.resources.interfaces.rest.transform;

import com.uitopic.restock.platform.resources.domain.model.entities.Supply;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.SupplyResource;

/**
 * Assembler to convert {@link com.uitopic.restock.platform.resources.domain.model.entities.Supply} entities
 * to {@link SupplyResource} DTOs within the resources bounded context.
 */
public class SupplyResourceFromEntityAssembler {

    public static SupplyResource toResourceFromEntity(Supply entity) {
        return new SupplyResource(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCategory() != null ? entity.getCategory().name() : null,
                entity.getIsPerishable() != null && entity.getIsPerishable(),
                entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null
        );
    }
}
