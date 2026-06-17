package com.uitopic.restock.platform.planning.interfaces.rest.transform;

import com.uitopic.restock.platform.planning.domain.model.aggregates.Product;
import com.uitopic.restock.platform.planning.interfaces.rest.resources.IngredientResource;
import com.uitopic.restock.platform.planning.interfaces.rest.resources.ProductAvailabilityResource;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Assembler that converts a {@link Product} aggregate and its maximum assemblable quantity
 * into a {@link ProductAvailabilityResource} DTO suitable for REST responses within the {@code planning} bounded context.
 */
public class ProductAvailabilityResourceFromEntityAssembler {

    /**
     * Transforms a {@link Product} entity and its max assemblable value into a {@link ProductAvailabilityResource} record.
     *
     * @param entity         the {@link Product} aggregate to transform; must not be {@code null}
     * @param maxAssemblable the maximum units that can be assembled with the current stock
     * @return a {@link ProductAvailabilityResource} containing the mapped data
     */
    public static ProductAvailabilityResource toResourceFromEntity(@NotNull Product entity, int maxAssemblable) {
        List<IngredientResource> ingredientResources = entity.getIngredients().stream()
                .map(i -> new IngredientResource(
                        i.getId(),
                        i.getProductId(),
                        i.getCustomSupplyId(),
                        i.getQuantity(),
                        i.getTotalCost()))
                .toList();

        return new ProductAvailabilityResource(
                entity.getId(),
                entity.getAccountId().getAccountId(),
                entity.getName(),
                entity.getDescription(),
                entity.getSku(),
                entity.getType() != null ? entity.getType().name() : null,
                entity.getImageUrl(),
                entity.getSellingPrice(),
                entity.getStatus() != null ? entity.getStatus().name() : null,
                ingredientResources,
                maxAssemblable
        );
    }
}
