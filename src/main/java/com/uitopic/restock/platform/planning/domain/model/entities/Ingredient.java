package com.uitopic.restock.platform.planning.domain.model.entities;

import lombok.*;

import java.math.BigDecimal;

/**
 * Entity representing an ingredient (or component) embedded within a
 * {@link com.uitopic.restock.platform.planning.domain.model.aggregates.Product} aggregate.
 *
 * <p>Each ingredient references a {@code customSupplyId} from the {@code resources} bounded context
 * and carries the quantity required and the calculated total cost for that quantity.
 * This entity is never persisted independently; it lives inside the {@code products} collection
 * as an array of sub-documents.</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@With
public class Ingredient {

    /** Unique identifier for this ingredient entry. Generated as an ObjectId string. */
    private String id;

    /**
     * Back-reference to the owning {@link com.uitopic.restock.platform.planning.domain.model.aggregates.Product}.
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
