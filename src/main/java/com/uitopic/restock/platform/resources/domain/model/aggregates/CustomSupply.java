package com.uitopic.restock.platform.resources.domain.model.aggregates;

import com.uitopic.restock.platform.resources.domain.model.commands.CreateCustomSupplyCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.UpdateCustomSupplyCommand;
import com.uitopic.restock.platform.resources.domain.model.entities.Supply;
import com.uitopic.restock.platform.resources.domain.model.valueobjects.StockRange;
import com.uitopic.restock.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.ImageURL;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.Money;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.UnitMeasurement;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Aggregate root representing a custom supply in the resources bounded context.
 *
 * A custom supply is created from a base supply and stores account-specific
 * information such as name, stock range, unit price, unit measurement,
 * description and optional picture URL.
 *
 * This aggregate stores both the supply identifier and the full supply data.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "custom_supplies")
public class CustomSupply extends AuditableAbstractAggregateRoot {

    /**
     * Account that owns this custom supply.
     */
    private AccountId accountId;

    /**
     * Custom display name of this supply.
     */
    private String name;

    /**
     * Identifier of the base supply used to create this custom supply.
     */
    private String supplyId;

    /**
     * Full base supply data associated with this custom supply.
     */
    private Supply supply;

    /**
     * Minimum and maximum stock range configured for this custom supply.
     */
    private StockRange stockRange;

    /**
     * Price per unit of this custom supply.
     */
    private Money unitPrice;

    /**
     * Description provided for this custom supply.
     */
    private String description;

    /**
     * Unit of measurement used by this custom supply.
     */
    private UnitMeasurement unitMeasurement;

    /**
     * Optional picture URL associated with this custom supply.
     */
    private ImageURL pictureUrl;

    /**
     * Creates a custom supply from a command, a loaded base supply and an optional picture.
     *
     * @param command command with the custom supply data
     * @param supply base supply loaded from persistence
     * @param pictureUrl uploaded picture URL, or null if no image was provided
     */
    public CustomSupply(CreateCustomSupplyCommand command, Supply supply, ImageURL pictureUrl) {
        if (command == null) {
            throw new IllegalArgumentException("Create custom supply command cannot be null");
        }

        if (supply == null) {
            throw new IllegalArgumentException("Supply cannot be null");
        }

        this.accountId = new AccountId(command.accountId());
        this.name = command.name();
        this.supplyId = command.supplyId();
        this.supply = supply;
        this.stockRange = command.stockRange();
        this.unitPrice = command.unitPrice();
        this.description = command.description();
        this.unitMeasurement = command.unitMeasurement();
        this.pictureUrl = pictureUrl;
    }

    /**
     * Partially updates this custom supply.
     *
     * The base supply ID and account ID are not modified by this operation.
     *
     * @param command command with optional updated fields
     * @param pictureUrl picture URL to keep or replace
     * @return updated custom supply
     */
    public CustomSupply update(
            UpdateCustomSupplyCommand command,
            ImageURL pictureUrl
    ) {
        if (command == null) {
            throw new IllegalArgumentException("Update custom supply command cannot be null");
        }

        if (command.name() != null) {
            if (command.name().isBlank()) {
                throw new IllegalArgumentException("Custom supply name cannot be blank");
            }
            this.name = command.name();
        }

        if (command.description() != null) {
            if (command.description().isBlank()) {
                throw new IllegalArgumentException("Description cannot be blank");
            }
            this.description = command.description();
        }

        if (command.stockRange() != null) {
            this.stockRange = command.stockRange();
        }

        if (command.unitPrice() != null) {
            this.unitPrice = command.unitPrice();
        }

        if (command.unitMeasurement() != null) {
            this.unitMeasurement = command.unitMeasurement();
        }

        if (pictureUrl != null) {
            this.pictureUrl = pictureUrl;
        }

        return this;
    }
}