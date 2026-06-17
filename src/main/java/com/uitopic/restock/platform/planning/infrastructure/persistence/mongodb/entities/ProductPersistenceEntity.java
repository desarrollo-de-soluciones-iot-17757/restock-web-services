package com.uitopic.restock.platform.planning.infrastructure.persistence.mongodb.entities;

import com.uitopic.restock.platform.planning.domain.model.valueobjects.ProductType;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.ResourceStatus;
import com.uitopic.restock.platform.shared.infrastructure.persistence.mongodb.entities.AuditableAbstractPersistenceEntity;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Persistence entity representing a product (either a recipe or a kit) in the MongoDB database.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "products")
public class ProductPersistenceEntity extends AuditableAbstractPersistenceEntity {

    /**
     * The tenant account that owns this product.
     * Stored as {@link AccountId} and serialised by a registered MongoDB converter.
     */
    private AccountId accountId;

    /**
     * Human-readable product name, unique per account.
     */
    private String name;

    /**
     * Optional description providing additional context about the product.
     */
    private String description;

    /**
     * Stock-Keeping Unit identifier.
     * Should be unique per account; uniqueness is enforced at the application layer.
     */
    private String sku;

    /** Discriminates between {@code RECIPE} and {@code KIT} products. */
    private ProductType type;

    /** Optional URL pointing to a product image (e.g., a Cloudinary URL). */
    private String imageUrl;

    /** Selling price of the finished product. Currency semantics are defined externally. */
    private BigDecimal sellingPrice;

    /** Lifecycle status of the product. */
    private ResourceStatus status;

    /**
     * Embedded list of ingredients/components that make up this product.
     * Initialised eagerly to avoid {@code null} checks throughout the aggregate.
     */
    @Builder.Default
    private List<IngredientPersistenceEntity> ingredients = new ArrayList<>();
}
