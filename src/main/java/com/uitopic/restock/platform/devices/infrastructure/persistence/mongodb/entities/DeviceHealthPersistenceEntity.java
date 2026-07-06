package com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.entities;

import com.uitopic.restock.platform.devices.domain.model.aggregates.DeviceHealth;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * MongoDB Persistence Entity for DeviceHealth.
 */
@Getter
@Setter
@NoArgsConstructor
@Document(collection = "devices_health")
public class DeviceHealthPersistenceEntity {

    @Id
    private String id;
    private String deviceId;
    private String branchId;
    private String alertType;
    private String metric;
    private String value;
    private String threshold;
    private String message;
    private Double cpuUsagePercentage;
    private Long memoryFreeBytes;
    private Double voltageVolts;
    private Double temperatureInCelsius;
    private Date timestamp;

    public DeviceHealthPersistenceEntity(DeviceHealth domainEntity) {
        this.id = domainEntity.getId();
        this.deviceId = domainEntity.getDeviceId();
        this.branchId = domainEntity.getBranchId();
        this.alertType = domainEntity.getAlertType();
        this.metric = domainEntity.getMetric();
        this.value = domainEntity.getValue();
        this.threshold = domainEntity.getThreshold();
        this.message = domainEntity.getMessage();
        this.cpuUsagePercentage = domainEntity.getCpuUsagePercentage();
        this.memoryFreeBytes = domainEntity.getMemoryFreeBytes();
        this.voltageVolts = domainEntity.getVoltageVolts();
        this.temperatureInCelsius = domainEntity.getTemperatureInCelsius();
        this.timestamp = domainEntity.getTimestamp();
    }

    public DeviceHealth toDomainEntity() {
        return new DeviceHealth(
                this.id,
                this.deviceId,
                this.branchId,
                this.alertType,
                this.metric,
                this.value,
                this.threshold,
                this.message,
                this.cpuUsagePercentage,
                this.memoryFreeBytes,
                this.voltageVolts,
                this.temperatureInCelsius,
                this.timestamp
        );
    }
}
