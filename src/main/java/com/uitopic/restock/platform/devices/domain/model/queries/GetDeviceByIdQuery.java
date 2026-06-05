package com.uitopic.restock.platform.devices.domain.model.queries;

public record GetDeviceByIdQuery(String deviceId) {

    public GetDeviceByIdQuery {
        if (deviceId == null || deviceId.isBlank())
            throw new IllegalArgumentException("Device ID cannot be null or blank");
    }
}
