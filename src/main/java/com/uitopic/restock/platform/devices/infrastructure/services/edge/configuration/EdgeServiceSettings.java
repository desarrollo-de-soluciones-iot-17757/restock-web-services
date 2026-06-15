package com.uitopic.restock.platform.devices.infrastructure.services.edge.configuration;

/**
 * This record represents the settings required to configure the Edge Service for device management. It includes the base URL for the Edge Service and the specific URL for device-related operations. These settings are essential for establishing communication between the application and the Edge Service, allowing for proper integration and functionality of device management features.
 */
public record EdgeServiceSettings(
        String baseUrl,
        String devicesUrl
) {
}
