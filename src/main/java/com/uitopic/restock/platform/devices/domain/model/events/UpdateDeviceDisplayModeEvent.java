package com.uitopic.restock.platform.devices.domain.model.events;

import com.uitopic.restock.platform.devices.domain.model.valueobjects.DisplayMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateDeviceDisplayModeEvent {

    @NotBlank
    private String macAddress;

    @NotNull
    private DisplayMode displayMode;

    public UpdateDeviceDisplayModeEvent(String macAddress, DisplayMode displayMode) {
        this.macAddress = macAddress;
        this.displayMode = displayMode;
    }
}
