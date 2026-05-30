package com.uitopic.restock.platform.resources.domain.model.entities;

import com.uitopic.restock.platform.shared.domain.model.entities.AuditableModel;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.UnitMeasurement;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "inventory_deductions")
public class InventoryDeduction extends AuditableModel {

    private String branchId;
    private String supplyId;
    private double quantity;
    private UnitMeasurement unit;
    private String reason;
    private String timestamp;
    private double remainingStock;
}
