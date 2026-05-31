package com.uitopic.restock.platform.resources.domain.model.entities;

import com.uitopic.restock.platform.shared.domain.model.entities.AuditableModel;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.UnitMeasurement;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents an inventory transfer between branches, capturing details such as the source and destination branch IDs, current stock levels at both branches, quantity transferred, unit of measurement, reason for transfer, and timestamp. This entity is essential for tracking inventory movements across branches and ensuring accurate inventory management within the system.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "inventory_transfers")
public class InventoryTransfer extends AuditableModel {

    /**
     * The ID of the branch from which the inventory is being transferred. This field is crucial for identifying the source branch involved in the transfer process and ensuring that inventory levels are accurately updated at the originating location.
     */
    private String fromBranchId;

    /**
     * The current stock level at the source branch before the transfer. This field is important for maintaining accurate inventory records and ensuring that the transfer does not result in negative stock levels at the source branch.
     */
    private Double fromBranchCurrentStock;

    /**
     * The ID of the branch to which the inventory is being transferred. This field is essential for identifying the destination branch involved in the transfer process and ensuring that inventory levels are accurately updated at the receiving location.
     */
    private String toBranchId;

    /**
     * The current stock level at the destination branch before the transfer. This field is important for maintaining accurate inventory records and ensuring that the transfer does not result in overstocking at the destination branch.
     */
    private Double toBranchCurrentStock;

    /**
     * The quantity of inventory being transferred from the source branch to the destination branch. This field is crucial for ensuring that the correct amount of inventory is moved between branches and that inventory levels are accurately updated at both locations after the transfer is completed.
     */
    private double quantityTransferred;

    /**
     * The unit of measurement for the quantity being transferred. This field is important for ensuring that the quantity is accurately represented and understood in the context of the inventory being transferred, allowing for consistent tracking and management of inventory across branches.
     */
    private UnitMeasurement unit;

    /**
     * The reason for the inventory transfer. This field is essential for providing context and justification for the transfer, allowing for better tracking and analysis of inventory movements across branches, and helping to identify patterns or issues that may arise in the inventory management process.
     */
    private String reason;

    /**
     * The timestamp of when the inventory transfer occurred. This field is crucial for maintaining accurate records of inventory movements, allowing for better tracking and analysis of transfer activities over time, and ensuring that inventory levels are updated in a timely manner across branches.
     */
    private String timestamp;
}