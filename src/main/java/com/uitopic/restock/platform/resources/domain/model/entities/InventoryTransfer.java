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
@Document(collection = "inventory_transfers")
public class InventoryTransfer extends AuditableModel {

    private String fromBranchId;
    private String toBranchId;
    private String supplyId;
    private double quantity;
    private UnitMeasurement unit;
    private String reason;
    private String status;
    private String completedAt;
}
