package com.uitopic.restock.platform.devices.domain.model.aggregates;

import com.uitopic.restock.platform.devices.domain.model.entities.DeviceThreshold;
import com.uitopic.restock.platform.devices.domain.model.events.DeviceConfiguredEvent;
import com.uitopic.restock.platform.devices.domain.model.valueobjects.*;
import com.uitopic.restock.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Device extends AbstractDomainAggregateRoot<Device> {

    private String id;
    private AccountId accountId;
    private String branchId;
    private String assignedBatchId;
    private String supplyThresholdId;
    private MacAddress macAddress;
    private String description;
    private DeviceSpecificationsInfo specifications;
    private WeightMeasurement weightMeasurement;
    private Double justifiedWithdrawnStock;
    private DeviceStatus status;

    @Builder
    public Device(String macAddress, String accountId, String description) {
        validateText(macAddress, "MAC address");
        validateText(accountId, "Account ID");

        this.macAddress = new MacAddress(macAddress);
        this.accountId = new AccountId(accountId);
        this.description = description;
        this.status = DeviceStatus.REGISTERED;
        this.justifiedWithdrawnStock = 0.0;
    }

    public void addSpecifications(String manufacturer, String model, String firmwareVersion) {
        this.specifications = new DeviceSpecificationsInfo(manufacturer, model, firmwareVersion);
    }

    public void assignBranch(String branchId) {
        validateText(branchId, "Branch ID");
        this.branchId = branchId;
    }

    public void assignBatch(String batchId) {
        validateText(batchId, "Batch ID");
        this.assignedBatchId = batchId;
    }

    public void assignSupplyThreshold(String thresholdId) {
        validateText(thresholdId, "Threshold ID");
        this.supplyThresholdId = thresholdId;
    }

    public void updateMeasurement(WeightMeasurement weightMeasurement) {
        if (weightMeasurement == null)
            throw new IllegalArgumentException("Weight measurement cannot be null");
        this.weightMeasurement = weightMeasurement;
    }

    public void confirmConfiguration(DeviceThreshold deviceThreshold) {
        if (specifications == null)
            throw new IllegalStateException("Specifications must be added before confirming configuration");
        if (branchId == null)
            throw new IllegalStateException("Branch must be assigned before confirming configuration");
        if (assignedBatchId == null)
            throw new IllegalStateException("Batch must be assigned before confirming configuration");
        if (supplyThresholdId == null)
            throw new IllegalStateException("Supply threshold must be assigned before confirming configuration");
        if (weightMeasurement == null)
            throw new IllegalStateException("Measurement must be configured before confirming configuration");

        this.status = DeviceStatus.CONFIGURED;
        this.registerDomainEvent(new DeviceConfiguredEvent(this.macAddress.address(), deviceThreshold));
    }

    public void activate() {
        if (status != DeviceStatus.CONFIGURED)
            throw new IllegalStateException("Device must be CONFIGURED to be activated");
        this.status = DeviceStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = DeviceStatus.INACTIVE;
    }

    public void updateJustifiedWithdrawnStock(Double amount) {
        if (amount == null || amount < 0)
            throw new IllegalArgumentException("Justified withdrawn stock cannot be null or negative");
        this.justifiedWithdrawnStock = amount;
    }

    public boolean isOperational() {
        return status == DeviceStatus.ACTIVE;
    }

    private static void validateText(String value, String fieldName) {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
    }
}
