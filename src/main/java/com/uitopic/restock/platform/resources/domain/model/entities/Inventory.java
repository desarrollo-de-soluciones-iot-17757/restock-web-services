package com.uitopic.restock.platform.resources.domain.model.entities;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.InventoryState;
import com.uitopic.restock.platform.resources.domain.model.valueobjects.MinimumStock;
import com.uitopic.restock.platform.resources.domain.model.valueobjects.Stock;
import com.uitopic.restock.platform.shared.domain.model.entities.AuditableModel;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents the inventory of a custom supply in a specific branch and batch. It contains information about the current stock, minimum stock level, and inventory state.
 * The inventory can be associated with a batch and a branch. The inventory state is determined based on the current stock and minimum stock level, and it can be used for inventory management and restocking purposes.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "inventories")
public class Inventory extends AuditableModel {

    /**
     * The batch ID of the inventory. It can be null if the inventory has not been assigned to a batch yet.
     */
    @Indexed
    private String batchId;

    /**
     * The branch where the inventory is located. It can be null if the inventory has not been assigned to a branch yet.
     */
    @Indexed
    private String branchId;

    /**
     * The current stock of the inventory. It can be null if the inventory has not been stocked yet.
     */
    private Stock currentStock;

    /**
     * The minimum stock level for this custom supply, which can be used for inventory management and restocking purposes.
     */
    private MinimumStock minimumStock;

    /**
     * The state of the inventory, which can be one of the following: IN_STOCK, OUT_OF_STOCK, OVERSTOCKED, or LOW_STOCK. It can be null if the inventory has not been assigned a state yet.
     */
    private InventoryState state = InventoryState.IN_STOCK;

    /**
     * Adds the given quantity to the current stock of the inventory. If the current stock is null, it will be initialized with the given quantity.
     *
     * @param quantity the quantity to add to the current stock
     */
    public void add(@Valid Stock quantity) {
        this.currentStock = this.currentStock == null ? quantity : this.currentStock.add(quantity);
    }

    /**
     * Subtracts the given quantity from the current stock of the inventory. If the current stock is null, it will be initialized with a stock of zero before subtracting the given quantity.
     *
     * @param quantity the quantity to subtract from the current stock
     */
    public void subtrack(@Valid Stock quantity) {
        this.currentStock = this.currentStock.subtrack(quantity);
    }

    /**
     * Updates the inventory state to LOW_STOCK. This method can be called when the current stock falls below the minimum stock level, indicating that the inventory is running low and may need to be restocked soon.
     */
    public void toLowStock() {
        this.state = InventoryState.LOW_STOCK;
    }

    /**
     * Updates the inventory state to IN_STOCK. This method can be called when the current stock is sufficient and above the minimum stock level, indicating that the inventory is in good condition and available for sale.
     */
    public void toInStock() {
        this.state = InventoryState.IN_STOCK;
    }

    /**
     * Updates the inventory state to OUT_OF_STOCK. This method can be called when the current stock is depleted and reaches zero, indicating that the inventory is out of stock and cannot be sold until it is restocked.
     */
    public void toOutOfStock() {
        this.state = InventoryState.OUT_OF_STOCK;
    }

    /**
     * Updates the inventory state to OVERSTOCK. This method can be called when the current stock exceeds a certain threshold, indicating that the inventory is overstocked and may need to be reduced or promoted to avoid overstocking issues.
     */
    public void toOverstocked() {
        this.state = InventoryState.OVERSTOCKED;
    }
}