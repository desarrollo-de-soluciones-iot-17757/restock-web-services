package com.uitopic.restock.platform.resources.domain.model.entities;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.InventoryState;
import com.uitopic.restock.platform.resources.domain.model.valueobjects.Stock;
import com.uitopic.restock.platform.shared.domain.model.entities.AuditableModel;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

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
     * The state of the inventory, which can be one of the following: IN_STOCK, OUT_OF_STOCK, OVERSTOCKED, or LOW_STOCK. It can be null if the inventory has not been assigned a state yet.
     */

    private InventoryState state;

    /**
     * Adds the given quantity to the current stock of the inventory. If the current stock is null, it will be initialized with the given quantity.
     *
     * @param quantity the quantity to add to the current stock
     */
    public void add(@Valid Stock quantity) {
        this.currentStock = this.currentStock == null ? quantity : this.currentStock.add(quantity);
    }

    public void subtrack(@Valid Stock quantity) {
        this.currentStock = this.currentStock.subtrack(quantity);
    }
}
