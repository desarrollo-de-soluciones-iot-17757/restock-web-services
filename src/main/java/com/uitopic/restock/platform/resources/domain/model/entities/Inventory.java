package com.uitopic.restock.platform.resources.domain.model.entities;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Batch;
import com.uitopic.restock.platform.resources.domain.model.aggregates.Branch;
import com.uitopic.restock.platform.resources.domain.model.valueobjects.Stock;
import com.uitopic.restock.platform.shared.domain.model.entities.AuditableModel;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entity representing the current inventory of a {@link Batch} at a specific {@link Branch}.
 *
 * <p>An {@code Inventory} record ties a batch to a branch and tracks the current stock level
 * at that location. Stock mutations are performed through {@link #addStock(int)} and
 * {@link #subtractStock(int)}, which delegate to the {@link Stock} value object to enforce
 * non-negative invariants.
 *
 * <p>Extends {@link com.uitopic.restock.platform.shared.domain.model.entities.AuditableModel}
 * to inherit {@code createdAt} and {@code updatedAt} audit timestamps.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "inventories")
public class Inventory extends AuditableModel {

    /** The batch whose stock is tracked by this inventory record. */
    private Batch batch;

    /** The branch at which this inventory is held. */
    private Branch branch;

    /** The current stock quantity of the batch at this branch. */
    private Stock currentStock;

    /**
     * Adds the given quantity to the current stock of this inventory.
     *
     * @param quantity the amount to add, must be non-negative
     * @return this inventory with the updated stock
     * @throws IllegalArgumentException if {@code quantity} is negative
     */
    public Inventory addStock(int quantity) {
        this.currentStock = this.currentStock.addStock(quantity);
        return this;
    }

    /**
     * Subtracts the given quantity from the current stock of this inventory.
     *
     * @param quantity the amount to subtract, must be non-negative and not exceed current stock
     * @return this inventory with the updated stock
     * @throws IllegalArgumentException if {@code quantity} is negative or exceeds current stock
     */
    public Inventory subtractStock(int quantity) {
        this.currentStock = this.currentStock.subtractStock(quantity);
        return this;
    }
}
