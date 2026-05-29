package com.uitopic.restock.platform.resources.domain.model.entities;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.SupplyNames;
import com.uitopic.restock.platform.shared.domain.model.entities.AuditableModel;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a supply item in the inventory system, extending the AuditableModel to include common auditing fields.
 * This class includes specific attributes related to the supply item, such as its name, description, category, and perishability.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "supplies")
public class Supply extends AuditableModel {

    /**
     * Name of the supply item.
     */
    private String name;

    /**
     * Detailed description of the supply item.
     */
    private String description;

    /**
     * Category of the supply item, represented as an enum for better type safety and maintainability.
     */
    private SupplyNames category;

    /**
     * Indicates whether the supply item is perishable, which can affect storage and handling requirements.
     */
    private Boolean isPerishable;
}
