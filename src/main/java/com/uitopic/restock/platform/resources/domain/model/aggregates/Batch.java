package com.uitopic.restock.platform.resources.domain.model.aggregates;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.Stock;
import com.uitopic.restock.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.Money;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Represents a batch of products in the inventory system. A batch is a specific quantity of a product that is received, stored, and managed as a unit within the inventory. Each batch has a unique code, associated product information, stock levels, and relevant dates for manufacturing, expiration, and entry into the inventory system. The Batch class provides methods for managing stock levels and tracking the details of each batch to ensure accurate inventory management.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "batches")
public class Batch extends AuditableAbstractAggregateRoot {

    /**
     * The unique code for the batch, which serves as an identifier for the batch. This code is used to track and manage the batch within the inventory system. It is a string value that can be generated based on specific rules or conventions defined by the business.
     */
    private String code;

    /**
     * The unique identifier for the product associated with this batch. This field is used to link the batch to a specific product in the inventory system, allowing for accurate tracking and management of stock levels for that product. It is a string value that corresponds to the product's unique identifier in the database.
     */
    private Stock initialStock;

    /**
     * The current stock level for the batch, which represents the quantity of items available in the batch. This field is updated as items are added or removed from the batch, allowing for real-time tracking of inventory levels. It is a Stock value object that encapsulates the quantity and provides methods for managing stock levels.
     */
    private Stock currentStock;

    /**
     * The unit of measurement for the items in the batch, which indicates how the quantity of items is measured (e.g., pieces, kilograms, liters). This field is important for ensuring that stock levels are accurately tracked and managed according to the appropriate unit of measurement. It is a UnitMeasurement value object that defines the type of measurement used for the batch.
     */
    private Money unitPurchaseCost;

    /**
     * The identifier for the custom supply related to this batch, which is used to link the batch to a specific supply order or source. This field is optional, as not all batches may be associated with a custom supply (e.g., if the batch was created from existing inventory). It is represented as a String that cannot be null.
     */
    @NotNull
    @NotBlank
    private String customSupplyId;

    /**
     * The identifier for the receiving branch where the batch was received, which is used to track the location of the batch within the inventory system. This field is optional, as not all batches may have a receiving branch (e.g., if the batch was created from existing inventory). It is represented as a String that cannot be null.
     */
    @NotNull
    @NotBlank
    private String receivingBranchId;

    /**
     * The identifier for the account associated with this batch, which is used to link the batch to a specific account in the inventory system. This field is important for tracking ownership and responsibility for the batch, as well as for managing inventory levels and costs associated with the batch. It is an AccountId value object that encapsulates the unique identifier for the account.
     */
    @Valid
    private AccountId accountId;

    /**
     * The date when the batch was manufactured, which is important for tracking the age of the batch and managing inventory based on expiration dates. This field is optional, as not all batches may have a manufacturing date available. It is represented as an Optional<LocalDate> to indicate that it may or may not be present.
     */
    private String manufacturingDate;

    /**
     * The date when the batch expires, which is crucial for managing inventory and ensuring that expired items are not sold or used. This field is optional, as not all batches may have an expiration date (e.g., non-perishable items). It is represented as an Optional<LocalDate> to indicate that it may or may not be present.
     */
    private String expirationDate;

    /**
     * Subtracks the specified quantity from the current stock of the batch. This method is used to update the stock levels when items are removed from the batch, ensuring that the current stock accurately reflects the quantity of items available. If the provided quantity is null, the method does nothing. Otherwise, it creates a new Stock instance with the updated quantity by subtracting the specified quantity from the current stock.
     *
     * @param quantity The quantity to be subtracted from the current stock, represented as a Stock value object. This parameter is validated to ensure that it is not null and that it represents a valid quantity of items to be removed from the batch.
     */
    public void subtrack(@Valid Stock quantity) {
        if (quantity == null) return;
        this.currentStock = new Stock(this.currentStock.stock() - quantity.stock(), this.currentStock.unitMeasurement());
    }
}