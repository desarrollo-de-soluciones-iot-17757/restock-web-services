package com.uitopic.restock.platform.resources.interfaces.rest.transform;

import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.CustomSupplyResource;

/**
 * Assembler to convert CustomSupply into CustomSupplyResource.
 */
public class CustomSupplyResourceFromEntityAssembler {

    public static CustomSupplyResource toResourceFromEntity(CustomSupply entity) {
        var supply = entity.getSupply();
        var pictureUrl = entity.getPictureUrl();

        return new CustomSupplyResource(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getSupplyId(),
                supply != null ? supply.getName() : null,
                supply != null ? supply.getCategory() : null,
                entity.getUnitPrice().getAmount().toString(),
                entity.getUnitPrice().getCurrencyCode(),
                entity.getUnitMeasurement().unitName(),
                entity.getStockRange() != null ? entity.getStockRange().minStock() : null,
                entity.getStockRange() != null ? entity.getStockRange().maxStock() : null,
                pictureUrl != null ? pictureUrl.url() : null,
                pictureUrl != null ? pictureUrl.publicId() : null,
                entity.getAccountId().getAccountId(),
                entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null
        );
    }
}