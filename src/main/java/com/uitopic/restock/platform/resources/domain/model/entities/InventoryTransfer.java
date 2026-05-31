package com.uitopic.restock.platform.resources.domain.model.entities;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Branch;
import com.uitopic.restock.platform.resources.domain.model.valueobjects.Stock;
import com.uitopic.restock.platform.shared.domain.model.entities.AuditableModel;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

/**
 * Entity representing a stock transfer event between two branches within the
 * resources bounded context.
 *
 * <p>Captures the movement of stock from an origin {@link Inventory} to a
 * destination {@link Branch}, recording the quantity transferred and the date
 * of the transfer. This provides an audit trail for inter-branch stock
 * movements.
 *
 * <p>Extends
 * {@link com.uitopic.restock.platform.shared.domain.model.entities.AuditableModel}
 * to inherit {@code createdAt} and {@code updatedAt} audit timestamps.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "inventory_transfers")
public class InventoryTransfer extends AuditableModel {

    /** The identifier of the branch from which stock is being transferred. */
    private String fromBranchId;

    /** The identifier of the branch that will receive the transferred stock. */
    private String toBranchId;

    /** The inventory at the origin branch from which stock is being transferred. */
    private Inventory originInventory;

    /** The branch that will receive the transferred stock. */
    private Branch destinationBranch;

    /** The quantity of stock transferred in this event. */
    private Stock quantityTransferred;

    /** The date on which this transfer was recorded. */
    private LocalDate transferDate;
}
