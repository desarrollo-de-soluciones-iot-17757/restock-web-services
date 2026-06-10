package com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.entities;

import com.uitopic.restock.platform.devices.domain.model.valueobjects.Humidity;
import com.uitopic.restock.platform.devices.domain.model.valueobjects.Temperature;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.infrastructure.persistence.mongodb.entities.AuditableAbstractPersistenceEntity;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.DeviceId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Document(collection = "device_thresholds")
public class DeviceThresholdPersistenceEntity extends AuditableAbstractPersistenceEntity {

    private AccountId accountId;

    private DeviceId deviceId;

    private String customSupplyId;

    private Double minStock;

    private Double maxStock;

    private Double anomalyThreshold;

    private Temperature temperature;

    private Humidity humidity;
}
