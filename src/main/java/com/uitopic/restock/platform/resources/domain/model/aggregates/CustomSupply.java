package com.uitopic.restock.platform.resources.domain.model.aggregates;

import com.uitopic.restock.platform.resources.domain.model.entities.Supply;
import com.uitopic.restock.platform.resources.domain.model.valueobjects.SupplyContent;
import com.uitopic.restock.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.ImageURL;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.Money;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.UnitMeasurement;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a custom supply in the system, which is a specific type of supply that can be created and managed by users.
 * This class extends the AuditableAbstractAggregateRoot to include auditing capabilities, allowing for tracking of creation and modification details.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "custom_supplies")
public class CustomSupply extends AuditableAbstractAggregateRoot {

    /**
     * The name of the custom supply, which can be used to identify and differentiate it from other supplies
     */
    private String name;

    /**
     * A brief description of the custom supply, which can provide additional information about the supply and its characteristics
     */
    private String description;

    /**
     * The category of the custom supply, which can be used for organizing and categorizing supplies (e.g., "Wines", "Fruits", "Vegetables", etc.)
     */
    private Supply category;

    /**
     * The price per unit of the custom supply, which can be used for pricing and billing purposes
     */
    private Money unitPrice;

    /**
     * The content or quantity of the custom supply, which can be used to specify how much of the supply is included in a single unit (e.g., "500ml", "1kg", "10 pieces", etc.)
     */
    private SupplyContent supplyContent;

    /**
     * The unit of measurement for the custom supply, such as "pieces", "boxes", "liters", etc.
     */
    private UnitMeasurement unitMeasurement;

    /**
     * Optional picture URL for the custom supply.
     */
    private ImageURL pictureUrl;

    /**
     * Indicates whether the custom supply is perishable or not, which can be important for inventory management and restocking purposes, especially for supplies that have a limited shelf life (e.g., food items, beverages, etc.)
     */
    private Boolean isPerishable;

    /**
     * Reference to the account that owns this custom supply.
     */
    private AccountId accountId;

    /**
     * Updates the custom supply with the provided details.
     *
     * @param description the new description of the custom supply
     * @param unitPrice the new unit price of the custom supply
     * @param supplyContent the new supply content of the custom supply
     * @param unitMeasurement the new unit measurement of the custom supply
     */
    public void update(@NotNull String description, Money unitPrice, SupplyContent supplyContent, UnitMeasurement unitMeasurement) {
        if (description != null) {
            this.description = description;
        }

        if (unitPrice != null) {
            this.unitPrice = unitPrice;
        }

        if (supplyContent != null) {
            this.supplyContent = supplyContent;
        }

        if (unitMeasurement != null) {
            this.unitMeasurement = unitMeasurement;
        }
    }
}
