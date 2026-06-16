package com.uitopic.restock.platform.devices.infrastructure.services.edge.services;

import com.uitopic.restock.platform.devices.application.internal.outboundservices.edgeservice.EdgeService;
import com.uitopic.restock.platform.devices.infrastructure.services.edge.configuration.EdgeServiceSettings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

/**
 * Infrastructure adapter that communicates with the Edge Service over HTTP.
 * Implements the {@link EdgeService} port so application/domain layers
 * remain decoupled from the Edge Service's transport details.
 */
@Service
@Slf4j
public class EdgeServiceImpl implements EdgeService {

    private final RestClient restClient;
    private final EdgeServiceSettings settings;

    public EdgeServiceImpl(EdgeServiceSettings settings) {
        this.settings = settings;
        this.restClient = RestClient.builder()
                .baseUrl(settings.baseUrl())
                .build();
    }

    @Override
    public void registerDevice(String macAddress, Double minStock, Double maxStock, Double minPercentage, Double maxPercentage, Double minCelsius, Double maxCelsius) {
        try {
            restClient.post()
                    .uri(settings.devicesUrl())
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(Map.of("device_id", macAddress))
                    .retrieve()
                    .toBodilessEntity();

            log.info("Device '{}' registered with edge service", macAddress);
        } catch (Exception e) {
            log.error("Failed to register device '{}' with edge service: {}", macAddress, e.getMessage());
        }
    }
}
