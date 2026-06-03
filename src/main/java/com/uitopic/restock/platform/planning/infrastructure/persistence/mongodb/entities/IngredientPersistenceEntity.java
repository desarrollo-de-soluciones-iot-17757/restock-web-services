package com.uitopic.restock.platform.planning.infrastructure.persistence.mongodb.entities;

import com.uitopic.restock.platform.planning.domain.model.aggregates.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;

/**
 * Persistence entity representing an ingredient (or component) embedded within a
 * {@link ProductPersistenceEntity} document in MongoDB.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientPersistenceEntity {

    /**
     * Unique identifier for this ingredient entry. Generated as an ObjectId string.
     */
    @MongoId(value = FieldType.OBJECT_ID)
    private String id;

    /**
     * Back-reference to the owning {@link Product}.
     * Stored for convenience querying without unwinding the array.
     */
    private String productId;

    /** Foreign key to {@code custom_supplies._id} in the {@code resources} bounded context. */
    private String customSupplyId;

    /** Amount of the custom supply required for one unit of the product. */
    private Double quantity;

    /**
     * Calculated cost: {@code quantity × unitPrice} of the referenced custom supply.
     * Computed at add-time by the application service using the cross-BC pricing port.
     */
    private BigDecimal totalCost;
}
