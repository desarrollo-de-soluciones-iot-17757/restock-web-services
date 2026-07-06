package com.uitopic.restock.platform.devices.domain.model.aggregates;

import com.uitopic.restock.platform.devices.domain.model.entities.DeviceThreshold;
import com.uitopic.restock.platform.devices.domain.model.events.DeviceCalibratedEvent;
import com.uitopic.restock.platform.devices.domain.model.events.DeviceConfiguredEvent;
import com.uitopic.restock.platform.devices.domain.model.events.DeviceRegisteredEvent;
import com.uitopic.restock.platform.devices.domain.model.valueobjects.*;
import com.uitopic.restock.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import lombok.*;

import java.security.SecureRandom;
import java.util.Base64;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Device extends AbstractDomainAggregateRoot<Device> {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final int DEVICE_TOKEN_BYTES = 32;

    private String id;
    private AccountId accountId;
    private String deviceToken;
    private String branchId;
    private String assignedBatchId;
    private String supplyThresholdId;
    private MacAddress macAddress;
    private String description;
    private DeviceSpecificationsInfo specifications;
    private WeightMeasurement weightMeasurement;
    private Double justifiedWithdrawnStock;
    private DeviceStatus status;

    private DisplayMode displayMode;

    @Builder
    public Device(String macAddress, String accountId, String description) {
        validateText(macAddress, "MAC address");
        validateText(accountId, "Account ID");

        this.macAddress = new MacAddress(macAddress);
        this.accountId = new AccountId(accountId);
        this.deviceToken = generateDeviceToken();
        this.description = description;
        this.status = DeviceStatus.REGISTERED;
        this.justifiedWithdrawnStock = 0.0;
        this.registerDomainEvent(new DeviceRegisteredEvent(
                this.macAddress.address(),
                this.accountId.getAccountId(),
                this.deviceToken
        ));
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
        validateConfigurationReady(deviceThreshold);
        if (status != DeviceStatus.REGISTERED && status != DeviceStatus.CONFIGURED)
            throw new IllegalStateException("Device must be REGISTERED before confirming configuration");

        this.status = DeviceStatus.CONFIGURED;
        this.registerDomainEvent(new DeviceConfiguredEvent(
                this.macAddress.address(),
                this.accountId.getAccountId(),
                deviceThreshold,
                this.assignedBatchId
        ));
    }

    public void confirmCalibration(DeviceThreshold deviceThreshold) {
        validateConfigurationReady(deviceThreshold);
        if (status != DeviceStatus.CONFIGURED && status != DeviceStatus.CALIBRATED && status != DeviceStatus.ACTIVE)
            throw new IllegalStateException("Device must be CONFIGURED before confirming calibration");
        if (weightMeasurement == null)
            throw new IllegalStateException("Measurement must be configured before confirming calibration");

        this.status = DeviceStatus.CALIBRATED;
        this.registerDomainEvent(new DeviceCalibratedEvent(
                this.macAddress.address(),
                this.accountId.getAccountId(),
                deviceThreshold,
                this.weightMeasurement,
                this.assignedBatchId
        ));
    }

    public void activate() {
        if (status != DeviceStatus.CALIBRATED)
            throw new IllegalStateException("Device must be CALIBRATED to be activated");
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

    public void updateDisplayMode(DisplayMode displayMode) {
        if (displayMode == null)
            throw new IllegalArgumentException("Display mode cannot be null");
        this.displayMode = displayMode;
    }

    public boolean isOperational() {
        return status == DeviceStatus.CALIBRATED || status == DeviceStatus.ACTIVE;
    }

    private static void validateText(String value, String fieldName) {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
    }

    private static String generateDeviceToken() {
        byte[] tokenBytes = new byte[DEVICE_TOKEN_BYTES];
        SECURE_RANDOM.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }

    private void validateConfigurationReady(DeviceThreshold deviceThreshold) {
        if (assignedBatchId == null)
            throw new IllegalStateException("Batch must be assigned before confirming configuration");
        if (supplyThresholdId == null)
            throw new IllegalStateException("Supply threshold must be assigned before confirming configuration");
        if (deviceThreshold == null)
            throw new IllegalArgumentException("Device threshold cannot be null");
    }
}
