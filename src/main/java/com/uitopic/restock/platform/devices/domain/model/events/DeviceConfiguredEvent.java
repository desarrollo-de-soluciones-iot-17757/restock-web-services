package com.uitopic.restock.platform.devices.domain.model.events;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class DeviceConfiguredEvent {

    @NotBlank
    @NotEmpty
    private String macAddress;

    public DeviceConfiguredEvent(String macAddress) {
        this.macAddress = macAddress;
    }
}
