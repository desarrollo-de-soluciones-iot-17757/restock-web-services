package com.uitopic.restock.platform.resources.domain.model.entities;

import com.uitopic.restock.platform.shared.domain.model.entities.AuditableModel;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.UnitMeasurement;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entity representing an inventory deduction record. This entity captures the details of inventory deductions, including the branch ID, batch ID, quantity deducted, unit of measurement, reason for deduction, timestamp of the deduction, and the remaining stock after the deduction.
 * It extends the AuditableModel to include auditing information such as creation and modification timestamps.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "inventory_deductions")
public class InventoryDeduction extends AuditableModel {

    /**
     * The ID of the branch where the inventory deduction occurred. This field is essential for tracking inventory deductions at specific branches and ensuring accurate inventory management across different locations.
     */
    private String branchId;

    /**
     * The ID of the batch from which the inventory was deducted. This field is crucial for tracking inventory deductions at the batch level, allowing for accurate inventory management and traceability of stock movements.
     */
    private String batchId;

    /**
     * The quantity of inventory that was deducted. This field is important for maintaining accurate inventory records and ensuring that the correct amount of stock is deducted from the inventory when necessary. The quantity should be a positive value representing the amount of stock that was removed from the inventory.
     */
    private Double quantity;

    /**
     * The unit of measurement for the quantity of inventory that was deducted. This field is essential for ensuring that the quantity is accurately represented and understood in the context of the inventory management system. The unit of measurement should be consistent with the units used in the inventory records to avoid confusion and maintain accurate inventory tracking.
     */
    private UnitMeasurement unit;

    /**
     * The reason for the inventory deduction. This field provides context for why the inventory was deducted, which can be important for auditing purposes and for understanding the circumstances that led to the deduction. The reason can help identify patterns or issues in inventory management and can be useful for making informed decisions about restocking and inventory control.
     */
    private String reason;

    /**
     * The timestamp of when the inventory deduction occurred. This field is crucial for maintaining accurate records of inventory deductions and for tracking the timing of stock movements. The timestamp can be used for auditing purposes, analyzing inventory trends, and ensuring that inventory records are up-to-date and accurate.
     */
    private String timestamp;

    /**
     * The remaining stock after the inventory deduction. This field is important for maintaining accurate inventory records and for understanding the impact of the deduction on the overall inventory levels. The remaining stock should be calculated based on the current stock levels and the quantity deducted, ensuring that inventory records reflect the correct stock levels after deductions are made.
     */
    private Double remainingStock;
}
