package com.uitopic.restock.platform.devices.domain.model.events;

import com.uitopic.restock.platform.devices.domain.model.entities.DeviceThreshold;
import com.uitopic.restock.platform.shared.domain.model.events.NotificationEvent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class DeviceConfiguredEvent implements NotificationEvent {

    @NotBlank
    @NotEmpty
    private String macAddress;

    private DeviceThreshold deviceThreshold;

    private String assignedBatchId;

    @NotBlank
    private String accountId;

    public DeviceConfiguredEvent(
            String macAddress,
            String accountId,
            DeviceThreshold deviceThreshold,
            String assignedBatchId
    ) {
        this.macAddress = macAddress;
        this.accountId = accountId;
        this.deviceThreshold = deviceThreshold;
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
        return "New device configured " + macAddress;
    }

    @Override
    public String notificationMessage() {
        return "Device with the mac address " + macAddress + " has been configured successfully";
    }
}
