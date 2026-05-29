package com.uitopic.restock.platform.resources.domain.model.aggregates;

import com.uitopic.restock.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.UnitMeasurement;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "batches")
public class Batch extends AuditableAbstractAggregateRoot {

    private AccountId accountId;
    private String branchId;
    private String customSupplyId;
    private double currentQuantity;
    private UnitMeasurement unit;
    private String expirationDate;
}
