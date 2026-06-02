package com.uitopic.restock.platform.resources.interfaces.rest.transform;

import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.CustomSupplyResource;
import jakarta.validation.constraints.NotNull;

/**
 * Assembler to convert
 * {@link com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply}
 * entities
 * to {@link CustomSupplyResource} DTOs within the resources bounded context.
 *
 * <p>
 * Maps the Supply entity to a {@link CustomSupplyResource.SupplyDto} primitive
 * nested object,
 * ensuring the Supply is not treated as a subdocument but as a complete value
 * object.
 */
public class CustomSupplyResourceFromEntityAssembler {

    /**
     * Static method to transform a CustomSupply entity into a CustomSupplyResource.
     *
     * <p>
     * Converts the Supply entity reference into a SupplyDto primitive object
     * containing
     * all necessary supply information.
     *
     * @param entity The CustomSupply entity to be transformed.
     * @return A CustomSupplyResource containing the data from the entity with
     *         Supply as a primitive DTO.
     */
    public static CustomSupplyResource toResourceFromEntity(@NotNull CustomSupply entity) {
        CustomSupplyResource.SupplyDto supplyDto = new CustomSupplyResource.SupplyDto(
                entity.getSupply().getId(),
                entity.getSupply().getName(),
                entity.getSupply().getDescription(),
                entity.getSupply().getCategory().name(),
                entity.getSupply().getIsPerishable());

        return new CustomSupplyResource(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                supplyDto,
                entity.getUnitPrice().getAmount().toString(),
                entity.getUnitPrice().getCurrencyCode(),
                entity.getContent().getContent(),
                entity.getUnitMeasurement().getUnitName(),
                entity.getImageUrl() != null ? entity.getImageUrl().getUrl() : null,
                entity.getAccountId().getAccountId(),
                entity.getIsPerishable());
    }
}
