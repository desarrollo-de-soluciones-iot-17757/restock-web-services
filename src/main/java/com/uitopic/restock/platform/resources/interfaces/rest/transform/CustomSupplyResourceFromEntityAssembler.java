package com.uitopic.restock.platform.resources.interfaces.rest.transform;

import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.CustomSupplyResource;
import jakarta.validation.constraints.NotNull;

/**
 * CustomSupplyResourceFromEntityAssembler is a utility class that provides a method to transform a CustomSupply entity into a CustomSupplyResource.
 * This class is used to convert the domain model (CustomSupply) into a format suitable for API responses (CustomSupplyResource).
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
                entity.getMinimumStock().getMinimumStock(),
                entity.getPictureUrl().getUrl(),
                entity.getAccountId().getAccountId()
        );
    }
}
