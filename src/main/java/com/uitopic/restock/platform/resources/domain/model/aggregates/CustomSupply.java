package com.uitopic.restock.platform.resources.domain.model.aggregates;

import com.uitopic.restock.platform.resources.domain.model.entities.Supply;
import com.uitopic.restock.platform.resources.domain.model.valueobjects.MinimumStock;
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
     * A brief description of the custom supply, which can provide additional information about the supply and its characteristics.
     */
    private String description;

    /**
     * The supply template this custom supply is based on (e.g., the base Supply catalog entry).
     */
    private Supply supply;

    /**
     * The price per unit of the custom supply, which can be used for pricing and billing purposes.
     */
    private Money unitPrice;

    /**
     * The content or quantity of the custom supply per unit (e.g., 500 for 500 ml, 1 for 1 kg).
     */
    private SupplyContent content;

    /**
     * The unit of measurement for the custom supply, such as "pieces", "boxes", "liters", etc.
     */
    private UnitMeasurement unitMeasurement;

    /**
     * The minimum stock threshold below which a restocking alert should be triggered.
     */
    private MinimumStock minimumStock;

    /**
     * URL of the image for this custom supply.
     */
    private ImageURL imageUrl;

    /**
     * Reference to the account that owns this custom supply.
     */
    private AccountId accountId;

    /**
     * Indicates whether this custom supply is perishable (i.e., has a limited shelf life).
     * This field overrides the perishability of the base {@link Supply} template at the account level.
     */
    private Boolean isPerishable;

    /**
     * Updates the perishable status of this custom supply.
     *
     * @param isPerishable {@code true} if the supply is perishable, {@code false} otherwise
     * @return this instance (fluent)
     */
    public CustomSupply updatePerishable(boolean isPerishable) {
        this.isPerishable = isPerishable;
        return this;
    }

    /**
     * Updates the custom supply with the provided details.
     *
     * @param minimumStock    the new minimum stock threshold
     * @param unitPrice       the new unit price of the custom supply
     * @param description     the new description of the custom supply
     * @param content         the new supply content (quantity per unit)
     * @param unitMeasurement the new unit of measurement
     * @return this instance (fluent)
     */
    public CustomSupply update(MinimumStock minimumStock, Money unitPrice, @NotNull String description,
                               SupplyContent content, UnitMeasurement unitMeasurement) {
        if (minimumStock != null) {
            this.minimumStock = minimumStock;
        }

        if (unitPrice != null) {
            this.unitPrice = unitPrice;
        }

        if (description != null) {
            this.description = description;
        }

        if (content != null) {
            this.content = content;
        }

        if (unitMeasurement != null) {
            this.unitMeasurement = unitMeasurement;
        }

        return this;
    }
}