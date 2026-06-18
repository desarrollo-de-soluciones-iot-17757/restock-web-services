package com.uitopic.restock.platform.devices.domain.model.events;

import com.uitopic.restock.platform.devices.domain.model.entities.DeviceThreshold;
import com.uitopic.restock.platform.devices.domain.model.valueobjects.WeightMeasurement;
import com.uitopic.restock.platform.shared.domain.model.events.NotificationEvent;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeviceCalibratedEvent implements NotificationEvent {

    @NotBlank
    private String macAddress;

    @NotBlank
    private String accountId;

    private DeviceThreshold deviceThreshold;

    private WeightMeasurement weightMeasurement;

    private String assignedBatchId;

    public DeviceCalibratedEvent(
            String macAddress,
            String accountId,
            DeviceThreshold deviceThreshold,
            WeightMeasurement weightMeasurement,
            String assignedBatchId
    ) {
        this.macAddress = macAddress;
        this.accountId = accountId;
        this.deviceThreshold = deviceThreshold;
        this.weightMeasurement = weightMeasurement;
        this.assignedBatchId = assignedBatchId;
    }

    @Override
    public String getSourceId() {
        return macAddress;
    }

    @Override
    public String getAlertLevelName() {
        return "OK";
    }

    @Override
    public String notificationTitle() {
        return "New device calibrated " + macAddress;
    }

    @Override
    public String notificationMessage() {
        return "Device with the mac address " + macAddress + " has been calibrated successfully";
    }
}
