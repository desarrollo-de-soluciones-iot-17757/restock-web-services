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
 * Aggregate root representing a user-defined supply item within an account in
 * the resources bounded context.
 *
 * <p>A {@code CustomSupply} is a supply product configured by an account owner, extending
 * a base {@link Supply} category template with account-specific details such as pricing,
 * content quantity, unit of measurement, and minimum stock threshold.
 *
 * <p>Custom supplies are the central resource tracked across {@link Batch} and
 * {@link com.uitopic.restock.platform.resources.domain.model.entities.Inventory} entities.
 * Extends {@link com.uitopic.restock.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot}
 * to inherit {@code createdAt} and {@code updatedAt} audit timestamps.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "custom_supplies")
public class CustomSupply extends AuditableAbstractAggregateRoot {

    /** The display name of this custom supply, unique within the owning account. */
    private String name;

    /** A human-readable description providing additional details about this supply. */
    private String description;

    /**
     * The base supply category this custom supply is derived from.
     * References a {@link Supply} entity (e.g., "Wines", "Fruits", "Vegetables").
     */
    private Supply category;

    /** The price per unit of this custom supply, used for cost tracking and billing. */
    private Money unitPrice;

    /**
     * The numeric content of a single unit (e.g., 500 for 500 ml, 1.5 for 1.5 kg).
     * The unit of measurement is captured separately in {@link #unitMeasurement}.
     */
    private SupplyContent supplyContent;

    /** The unit of measurement for this supply (e.g., "kg", "liters", "pieces"). */
    private UnitMeasurement unitMeasurement;

    /**
     * The minimum stock threshold for this supply.
     * When current stock falls below this value, the inventory state transitions to
     * {@link com.uitopic.restock.platform.resources.domain.model.valueobjects.InventoryState#LOWSTOCK}.
     */
    private MinimumStock minimumStock;

    /** Optional URL pointing to a picture that represents this custom supply. */
    private ImageURL pictureUrl;

    /** The identifier of the account that owns this custom supply. */
    private AccountId accountId;

    /**
     * Applies a partial update to this custom supply, only overwriting fields that are non-null.
     *
     * @param description     the new description, or {@code null} to keep the existing one
     * @param unitPrice       the new unit price, or {@code null} to keep the existing one
     * @param supplyContent   the new supply content, or {@code null} to keep the existing one
     * @param unitMeasurement the new unit of measurement, or {@code null} to keep the existing one
     * @param minimumStock    the new minimum stock threshold, or {@code null} to keep the existing one
     */
    public void update(@NotNull String description, Money unitPrice, SupplyContent supplyContent, UnitMeasurement unitMeasurement, MinimumStock minimumStock) {
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

        if (minimumStock != null) {
            this.minimumStock = minimumStock;
        }
    }
}
