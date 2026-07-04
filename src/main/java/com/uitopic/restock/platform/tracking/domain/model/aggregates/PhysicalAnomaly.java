package com.uitopic.restock.platform.tracking.domain.model.aggregates;

import com.uitopic.restock.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.DeviceId;
import com.uitopic.restock.platform.tracking.domain.model.commands.CreatePhysicalAnomalyCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Aggregate root representing a physical anomaly registered by an IoT scale device.
 */
@Getter
@Setter
@NoArgsConstructor
public class PhysicalAnomaly extends AbstractDomainAggregateRoot<PhysicalAnomaly> {

    private String id;
    private DeviceId deviceId;
    private Double registeredValue;
    private Instant timestamp;

    public PhysicalAnomaly(DeviceId deviceId, Double registeredValue, Instant timestamp) {
        this.deviceId = deviceId;
        this.registeredValue = registeredValue;
        this.timestamp = timestamp != null ? timestamp : Instant.now();
    }

    public PhysicalAnomaly(CreatePhysicalAnomalyCommand command) {
        this.deviceId = new DeviceId(command.deviceId());
        this.registeredValue = command.registeredValue();
        this.timestamp = command.timestamp() != null ? command.timestamp() : Instant.now();
    }
}
