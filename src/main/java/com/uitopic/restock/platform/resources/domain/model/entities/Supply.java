package com.uitopic.restock.platform.resources.domain.model.entities;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.SupplyNames;
import com.uitopic.restock.platform.shared.domain.model.entities.AuditableModel;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entity representing a base supply template in the resources bounded context.
 *
 * <p>A {@code Supply} acts as a catalog entry that defines a category of supply item
 * (e.g., POTATO, TOMATO) along with its description and perishability flag.
 * {@link com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply} aggregates
 * reference a {@code Supply} as their {@code category}, allowing account-specific customization
 * on top of a shared supply template.
 *
 * <p>Extends {@link com.uitopic.restock.platform.shared.domain.model.entities.AuditableModel}
 * to inherit {@code createdAt} and {@code updatedAt} audit timestamps.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "supplies")
public class Supply extends AuditableModel {

    /** The display name of this supply template. */
    private String name;

    /** A detailed description of this supply item and its characteristics. */
    private String description;

    /**
     * The category of this supply, represented as a {@link SupplyNames} enum value
     * for type safety and maintainability.
     */
    private SupplyNames category;

    /**
     * Indicates whether this supply item is perishable.
     * Perishable supplies require expiration date tracking in associated {@link com.uitopic.restock.platform.resources.domain.model.aggregates.Batch} records.
     */
    private Boolean isPerishable;
}
