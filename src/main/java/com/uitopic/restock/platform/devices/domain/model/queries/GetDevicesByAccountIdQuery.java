package com.uitopic.restock.platform.devices.domain.model.queries;

public record GetDevicesByAccountIdQuery(String accountId) {

    public GetDevicesByAccountIdQuery {
        if (accountId == null || accountId.isBlank())
            throw new IllegalArgumentException("Account ID cannot be null or blank");
    }
}
