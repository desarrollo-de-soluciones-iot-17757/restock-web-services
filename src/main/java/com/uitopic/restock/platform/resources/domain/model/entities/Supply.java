package com.uitopic.restock.platform.resources.domain.model.entities;

import com.uitopic.restock.platform.shared.domain.model.entities.AuditableModel;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entity representing a base supply template in the resources bounded context.
 *
 * A supply works as a catalog item that can be used as the base for custom
 * supplies. It stores general information such as name, description, category
 * and whether it is perishable.
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "supplies")
public class Supply extends AuditableModel {

    /**
     * Display name of the supply template.
     */
    private String name;

    /**
     * Description of the supply and its main characteristics.
     */
    private String description;

    /**
     * Category or type assigned to this supply.
     */
    private String category;

    /**
     * Indicates whether this supply requires expiration date tracking.
     */
    private Boolean isPerishable;

    /**
     * Creates a supply template with its main catalog information.
     *
     * @param name display name of the supply
     * @param description description of the supply
     * @param category category assigned to the supply
     * @param isPerishable whether the supply is perishable
     */
    public Supply(String name, String description, String category, Boolean isPerishable) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Supply name cannot be null or blank");
        }

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Supply description cannot be null or blank");
        }

        if (category == null) {
            throw new IllegalArgumentException("Supply category cannot be null");
        }

        if (isPerishable == null) {
            throw new IllegalArgumentException("Supply perishability cannot be null");
        }

        this.name = name;
        this.description = description;
        this.category = category;
        this.isPerishable = isPerishable;
    }
}