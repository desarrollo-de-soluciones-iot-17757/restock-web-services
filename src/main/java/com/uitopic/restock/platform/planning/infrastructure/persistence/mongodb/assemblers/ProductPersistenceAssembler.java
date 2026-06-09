package com.uitopic.restock.platform.planning.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.planning.domain.model.aggregates.Product;
import com.uitopic.restock.platform.planning.infrastructure.persistence.mongodb.entities.ProductPersistenceEntity;

/**
 * Utility class for converting between {@link Product} domain objects and {@link ProductPersistenceEntity} persistence entities.
 * <p>
 * This class provides static methods to perform the necessary transformations for both directions of conversion.
 * It is designed to be stateless and non-instantiable, serving solely as a collection of related conversion
 */
public final class ProductPersistenceAssembler {

    // Private constructor to prevent instantiation
    private ProductPersistenceAssembler() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Converts a persistence entity to a domain Product. If the input entity is {@code null}, this method returns {@code null}.
     *
     * @param entity the persistence entity to convert; may be {@code null}
     * @return a Product domain object representing the given persistence entity, or {@code null} if the input is {@code null}
     */
    public static Product toDomainFromPersistence(ProductPersistenceEntity entity) {
        if (entity == null) return null;

        var product = new Product();
        product.setId(entity.getId());
        product.setAccountId(entity.getAccountId());
        product.setName(entity.getName());
        product.setDescription(entity.getDescription());
        product.setSku(entity.getSku());
        product.setType(entity.getType());
        product.setImageUrl(entity.getImageUrl());
        product.setSellingPrice(entity.getSellingPrice());

        var ingredients = entity.getIngredients().stream()
                .map(IngredientPersistenceAssembler::toDomainFromPersistence)
                .toList();
        product.setIngredients(ingredients);

        return product;
    }

    /**
     * Converts a domain Product to a persistence entity. If the product has an ID, it will be included in the entity;
     *
     * @param product the domain product to convert; must not be {@code null}
     * @return a ProductPersistenceEntity representing the given domain product, or {@code null} if the input is {@code null}
     */
    public static ProductPersistenceEntity toPersistenceFromDomain(Product product) {
        if (product == null) return null;

        var entity = new ProductPersistenceEntity();

        if (product.getId() != null) {
            entity.setId(product.getId());
        }
        entity.setAccountId(product.getAccountId());
        entity.setName(product.getName());
        entity.setDescription(product.getDescription());
        entity.setSku(product.getSku());
        entity.setType(product.getType());
        entity.setImageUrl(product.getImageUrl());
        entity.setSellingPrice(product.getSellingPrice());

        var ingredientEntities = product.getIngredients().stream()
                .map(IngredientPersistenceAssembler::toPersistenceFromDomain)
                .toList();
        entity.setIngredients(ingredientEntities);

        return entity;
    }
}
