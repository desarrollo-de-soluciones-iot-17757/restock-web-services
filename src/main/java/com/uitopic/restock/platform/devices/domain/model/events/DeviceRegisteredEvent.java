package com.uitopic.restock.platform.devices.domain.model.events;

import com.uitopic.restock.platform.shared.domain.model.events.NotificationEvent;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeviceRegisteredEvent implements NotificationEvent {

    @NotBlank
    private String macAddress;

    @NotBlank
    private String accountId;

    @NotBlank
    private String deviceToken;

    public DeviceRegisteredEvent(String macAddress, String accountId, String deviceToken) {
        this.macAddress = macAddress;
        this.accountId = accountId;
        this.deviceToken = deviceToken;
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
        return "New device registered " + macAddress;
    }

    @Override
    public String notificationMessage() {
        return "Device with the mac address " + macAddress + " has been registered successfully";
    }
}
