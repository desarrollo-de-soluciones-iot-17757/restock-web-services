package com.uitopic.restock.platform.planning.interfaces.rest.transform;

import com.uitopic.restock.platform.planning.domain.model.aggregates.Product;
import com.uitopic.restock.platform.planning.interfaces.rest.resources.IngredientResource;
import com.uitopic.restock.platform.planning.interfaces.rest.resources.ProductResource;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Assembler that converts a {@link Product} aggregate into a {@link ProductResource} DTO
 * suitable for REST responses within the {@code planning} bounded context.
 */
public class ProductResourceFromEntityAssembler {

    /**
     * Transforms a {@link Product} entity into a {@link ProductResource} record.
     *
     * @param entity the {@link Product} aggregate to transform; must not be {@code null}
     * @return a {@link ProductResource} containing the mapped data
     */
    public static ProductResource toResourceFromEntity(@NotNull Product entity) {
        List<IngredientResource> ingredientResources = entity.getIngredients().stream()
                .map(i -> new IngredientResource(
                        i.getId(),
                        i.getProductId(),
                        i.getCustomSupplyId(),
                        i.getQuantity(),
                        i.getTotalCost()))
                .toList();

        return new ProductResource(
                entity.getId(),
                entity.getAccountId().getAccountId(),
                entity.getName(),
                entity.getDescription(),
                entity.getSku(),
                entity.getType() != null ? entity.getType().name() : null,
                entity.getImageUrl(),
                entity.getSellingPrice(),
                entity.getStatus() != null ? entity.getStatus().name() : null,
                ingredientResources
        );
    }
}
