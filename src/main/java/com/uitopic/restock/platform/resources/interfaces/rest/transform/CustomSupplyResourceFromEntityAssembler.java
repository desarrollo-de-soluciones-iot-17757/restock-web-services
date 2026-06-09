package com.uitopic.restock.platform.resources.interfaces.rest.transform;

import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.CustomSupplyResource;
import jakarta.validation.constraints.NotNull;

/**
 * Assembler to convert CustomSupply aggregates into CustomSupplyResource responses.
 */
public class CustomSupplyResourceFromEntityAssembler {

    /**
     * Converts a CustomSupply aggregate into a REST resource.
     *
     * @param entity custom supply aggregate
     * @return custom supply resource
     */
    public static CustomSupplyResource toResourceFromEntity(@NotNull CustomSupply entity) {
        return new CustomSupplyResource(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),

                entity.getUnitPrice() != null
                        ? entity.getUnitPrice().getAmount().toString()
                        : null,

                entity.getUnitPrice() != null
                        ? entity.getUnitPrice().getCurrencyCode()
                        : null,

                entity.getStockRange() != null
                        ? entity.getStockRange().minStock()
                        : null,

                entity.getStockRange() != null
                        ? entity.getStockRange().maxStock()
                        : null,

                entity.getUnitMeasurement() != null
                        ? entity.getUnitMeasurement().unitName()
                        : null,

                entity.getUnitMeasurement() != null
                        ? entity.getUnitMeasurement().abbreviation()
                        : null,

                entity.getPictureUrl() != null
                        ? entity.getPictureUrl().url()
                        : null,

                entity.getPictureUrl() != null
                        ? entity.getPictureUrl().publicId()
                        : null,

                entity.getAccountId() != null
                        ? entity.getAccountId().getAccountId()
                        : null,

                entity.getSupply() != null
                        ? SupplyResourceFromEntityAssembler.toResourceFromEntity(entity.getSupply())
                        : null
        );
    }
}