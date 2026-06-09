package com.uitopic.restock.platform.devices.domain.model.valueobjects;

public record MacAddress(String address) {

    private static final String MAC_PATTERN = "^([0-9A-Fa-f]{2}:){5}[0-9A-Fa-f]{2}$";

    public MacAddress {
        if (address == null || address.isBlank())
            throw new IllegalArgumentException("MAC address cannot be null or blank");
        if (!address.matches(MAC_PATTERN))
            throw new IllegalArgumentException("Invalid MAC address format: " + address);
    }
}
