package com.uitopic.restock.platform.devices.domain.model.queries;

public record GetDeviceThresholdsByAccountIdQuery(String accountId) {

    public GetDeviceThresholdsByAccountIdQuery {
        if (accountId == null || accountId.isBlank())
            throw new IllegalArgumentException("Account ID cannot be null or blank");
    }
}
