package com.uitopic.restock.platform.devices.domain.model.queries;

public record GetDeviceByMacAddressQuery(String macAddress) {

    public GetDeviceByMacAddressQuery {
        if (macAddress == null || macAddress.isBlank())
            throw new IllegalArgumentException("MAC address cannot be null or blank");
    }
}
