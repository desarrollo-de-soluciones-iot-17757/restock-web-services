package com.uitopic.restock.platform.resources.interfaces.rest.transform;

import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.CustomSupplyResource;
import jakarta.validation.constraints.NotNull;

/**
 * Assembler to convert {@link com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply} entities
 * to {@link CustomSupplyResource} DTOs within the resources bounded context.
 */
public class CustomSupplyResourceFromEntityAssembler {

    /**
     * Static method to transform a CustomSupply entity into a CustomSupplyResource.
     *
     * @param entity The CustomSupply entity to be transformed.
     * @return A CustomSupplyResource containing the data from the entity.
     */
    public static CustomSupplyResource toResourceFromEntity(@NotNull CustomSupply entity) {
        return new CustomSupplyResource(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCategory().getName(),
                entity.getUnitPrice().getAmount().toString(),
                entity.getUnitPrice().getCurrencyCode(),
                entity.getSupplyContent().getContent(),
                entity.getUnitMeasurement().getUnitName(),
                entity.getPictureUrl() != null ? entity.getPictureUrl().getUrl() : null,
                entity.getAccountId().getAccountId()
        );
    }
}
