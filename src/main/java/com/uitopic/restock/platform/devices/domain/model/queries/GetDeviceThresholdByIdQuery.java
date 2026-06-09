package com.uitopic.restock.platform.devices.domain.model.queries;

public record GetDeviceThresholdByIdQuery(String thresholdId) {

    public GetDeviceThresholdByIdQuery {
        if (thresholdId == null || thresholdId.isBlank())
            throw new IllegalArgumentException("Threshold ID cannot be null or blank");
    }
}
