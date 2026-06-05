package com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.entities;

import com.uitopic.restock.platform.devices.domain.model.valueobjects.DeviceSpecificationsInfo;
import com.uitopic.restock.platform.devices.domain.model.valueobjects.DeviceStatus;
import com.uitopic.restock.platform.devices.domain.model.valueobjects.MacAddress;
import com.uitopic.restock.platform.devices.domain.model.valueobjects.WeightMeasurement;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.infrastructure.persistence.mongodb.entities.AuditableAbstractPersistenceEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Document(collection = "devices")
public class DevicePersistenceEntity extends AuditableAbstractPersistenceEntity {

    private AccountId accountId;

    private String branchId;

    private String assignedBatchId;

    private String supplyThresholdId;

    @Indexed(unique = true)
    private MacAddress macAddress;

    private String description;

    private DeviceSpecificationsInfo specifications;

    private WeightMeasurement weightMeasurement;

    private Double justifiedWithdrawnStock;

    private DeviceStatus status;
}
