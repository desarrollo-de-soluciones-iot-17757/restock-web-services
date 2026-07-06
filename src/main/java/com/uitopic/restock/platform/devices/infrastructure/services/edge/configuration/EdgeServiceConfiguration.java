package com.uitopic.restock.platform.devices.infrastructure.services.edge.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EdgeServiceConfiguration {

    @Value("${edge.service.url}")
    private String baseUrl;

    @Value("${edge.service.iam.url}")
    private String iamDevicesEndpointUrl;

    @Value("${edge.service.device-thresholds.url}")
    private String deviceThresholdsEndpointUrl;

    @Value("${edge.service.device-display-mode.url}")
    private String deviceDisplayModeEndpointUrl;

    @Bean
    public EdgeServiceSettings edgeServiceSettings() {
        return new EdgeServiceSettings(
                baseUrl,
                iamDevicesEndpointUrl,
                deviceThresholdsEndpointUrl,
                deviceDisplayModeEndpointUrl
        );
    }
}
