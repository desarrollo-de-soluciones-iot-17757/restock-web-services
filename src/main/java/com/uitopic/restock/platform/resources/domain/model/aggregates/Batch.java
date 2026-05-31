package com.uitopic.restock.platform.resources.domain.model.aggregates;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.Stock;
import com.uitopic.restock.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.Money;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

/**
 * Aggregate root representing a batch of a {@link CustomSupply} received at a
 * {@link Branch} within the resources bounded context.
 *
 * <p>A batch captures a single purchase or delivery event, recording the initial
 * and current stock quantities, unit purchase cost, and relevant dates
 * (fabrication, expiration, entry). Stock changes are applied through
 * {@link #updateStock(int)} to keep the current stock consistent with
 * deductions and transfers recorded in
 * {@link com.uitopic.restock.platform.resources.domain.model.entities.InventoryDeduction}
 * and
 * {@link com.uitopic.restock.platform.resources.domain.model.entities.InventoryTransfer}.
 *
 * <p>Extends
 * {@link com.uitopic.restock.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot}
 * to inherit {@code createdAt} and {@code updatedAt} audit timestamps.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "batches")
public class Batch extends AuditableAbstractAggregateRoot {

    /** The identifier of the account that owns this batch. */
    private AccountId accountId;

    /** The identifier of the branch where this batch is stored. */
    private String branchId;

    /** The identifier of the custom supply that this batch contains. */
    private String customSupplyId;

    /**
     * A human-readable code used to identify and track this batch (e.g., a lot
     * number).
     */
    private String code;

    /** The stock quantity at the time this batch was received. */
    private Stock initialStock;

    /** The current remaining stock quantity in this batch. */
    private Stock currentStock;

    /** The cost paid per unit when this batch was purchased. */
    private Money unitPurchaseCost;

    /** The date on which this batch was manufactured or produced. */
    private LocalDate fabricationDate;

    /**
     * The date after which this batch should no longer be used (for perishable
     * supplies).
     */
    private LocalDate expirationDate;

    /** The date on which this batch was received at the branch. */
    private LocalDate entryDate;

    /** The custom supply that this batch contains. */
    private CustomSupply customSupply;

    /** The branch at which this batch was received and is stored. */
    private Branch receivingBranch;

    /**
     * Updates the current stock of this batch by the given quantity.
     * Positive values add stock; negative values subtract stock.
     *
     * @param quantity the amount to add (positive) or subtract (negative)
     * @return this batch with the updated current stock
     * @throws IllegalArgumentException if the resulting stock would be negative
     */
    public Batch updateStock(int quantity) {
        if (quantity >= 0) {
            this.currentStock = this.currentStock.addStock(quantity);
        } else {
            this.currentStock = this.currentStock.subtractStock(-quantity);
        }
        return this;
    }
}
