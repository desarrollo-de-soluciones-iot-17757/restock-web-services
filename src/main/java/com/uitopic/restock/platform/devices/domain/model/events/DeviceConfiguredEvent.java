package com.uitopic.restock.platform.devices.domain.model.events;

import com.uitopic.restock.platform.devices.domain.model.entities.DeviceThreshold;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class DeviceConfiguredEvent {

    @NotBlank
    @NotEmpty
    private String macAddress;

    private DeviceThreshold deviceThreshold;

    public DeviceConfiguredEvent(String macAddress, DeviceThreshold deviceThreshold) {
        this.macAddress = macAddress;
    }
}
