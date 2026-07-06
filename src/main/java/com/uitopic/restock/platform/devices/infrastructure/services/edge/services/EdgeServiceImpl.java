package com.uitopic.restock.platform.devices.infrastructure.services.edge.services;

import com.uitopic.restock.platform.devices.application.internal.outboundservices.edgeservice.EdgeService;
import com.uitopic.restock.platform.devices.domain.model.valueobjects.DisplayMode;
import com.uitopic.restock.platform.devices.domain.model.valueobjects.WeightMeasurement;
import com.uitopic.restock.platform.devices.infrastructure.services.edge.configuration.EdgeServiceSettings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Infrastructure adapter that communicates with the Edge Service over HTTP.
 * Implements the {@link EdgeService} port so application/domain layers
 * remain decoupled from the Edge Service's transport details.
 */
@Service
@Slf4j
public class EdgeServiceImpl implements EdgeService {

    private static final double CONFIGURATION_PLACEHOLDER_UNIT_MEASUREMENT = 1.0;

    private final RestClient restClient;
    private final EdgeServiceSettings settings;

    public EdgeServiceImpl(EdgeServiceSettings settings) {
        this.settings = settings;
        this.restClient = RestClient.builder()
                .baseUrl(settings.baseUrl())
                .build();
    }

    @Override
    public void registerDevice(String macAddress, String deviceToken) {
        try {
            restClient.post()
                    .uri(settings.iamDevicesUrl())
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "device_id", macAddress,
                            "device_token", deviceToken
                    ))
                    .retrieve()
                    .toBodilessEntity();

            log.info("Device '{}' registered with edge service", macAddress);
        } catch (Exception e) {
            log.error("Failed to register device '{}' with edge service: {}", macAddress, e.getMessage());
        }
    }

    @Override
    public void configureDevice(
            String macAddress,
            String assignedBatchId,
            Double minStock,
            Double maxStock,
            Double minTemperatureCelsius,
            Double maxTemperatureCelsius,
            Double minHumidityPercentage,
            Double maxHumidityPercentage,
            Double anomalyThreshold
    ) {
        try {
            var body = buildThresholdBody(
                    assignedBatchId,
                    minStock,
                    maxStock,
                    minTemperatureCelsius,
                    maxTemperatureCelsius,
                    minHumidityPercentage,
                    maxHumidityPercentage,
                    anomalyThreshold
            );

            log.info(
                    "Configuring device '{}' with edge service. baseUrl={}, endpoint={}",
                    macAddress,
                    settings.baseUrl(),
                    settings.deviceThresholdsUrl()
            );

            restClient.post()
                    .uri(settings.deviceThresholdsUrl(), Map.of("device_id", macAddress))
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();

            log.info("Device '{}' configured with edge service", macAddress);
        } catch (Exception e) {
            log.error(
                    "Failed to configure device '{}' with edge service. baseUrl={}, endpoint={}, error={}",
                    macAddress,
                    settings.baseUrl(),
                    settings.deviceThresholdsUrl(),
                    e.getMessage()
            );
        }
    }

    @Override
    public void calibrateDevice(
            String macAddress,
            String assignedBatchId,
            Double minStock,
            Double maxStock,
            Double minTemperatureCelsius,
            Double maxTemperatureCelsius,
            Double minHumidityPercentage,
            Double maxHumidityPercentage,
            Double anomalyThreshold,
            WeightMeasurement weightMeasurement
    ) {
        try {
            var body = buildThresholdBody(
                    assignedBatchId,
                    minStock,
                    maxStock,
                    minTemperatureCelsius,
                    maxTemperatureCelsius,
                    minHumidityPercentage,
                    maxHumidityPercentage,
                    anomalyThreshold
            );
            if (weightMeasurement != null) {
                body.put("custom_supply_unit_measurement", weightMeasurement.unitStockWeight());
                body.put("unit_stock_weight", weightMeasurement.unitStockWeight());
                body.put("tare_weight", weightMeasurement.tareWeight());
                putIfPresent(body, "gross_weight", weightMeasurement.grossWeight());
                putIfPresent(body, "calibration_date", weightMeasurement.calibrationDate() != null
                        ? weightMeasurement.calibrationDate().toString()
                        : null);
                body.put("weight_unit_name", weightMeasurement.weightUnit().unitName());
                body.put("weight_unit_abbreviation", weightMeasurement.weightUnit().abbreviation());
            }

            log.info(
                    "Calibrating device '{}' with edge service. baseUrl={}, endpoint={}",
                    macAddress,
                    settings.baseUrl(),
                    settings.deviceThresholdsUrl()
            );

            restClient.put()
                    .uri(settings.deviceThresholdsUrl(), Map.of("device_id", macAddress))
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();

            log.info("Device '{}' calibrated with edge service", macAddress);
        } catch (Exception e) {
            log.error(
                    "Failed to calibrate device '{}' with edge service. baseUrl={}, endpoint={}, error={}",
                    macAddress,
                    settings.baseUrl(),
                    settings.deviceThresholdsUrl(),
                    e.getMessage()
            );
        }
    }

    @Override
    public void changeDisplayMode(String macAddress, DisplayMode displayMode) {
        try {
            var displayModePayload = displayMode.name();

            log.info(
                    "Changing display mode for device '{}' with edge service. baseUrl={}, endpoint={}",
                    macAddress,
                    settings.baseUrl(),
                    settings.deviceDisplayModeUrl()
            );

            restClient.patch()
                    .uri(settings.deviceDisplayModeUrl(), Map.of("device_id", macAddress))
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(Map.of("display_mode", displayModePayload))
                    .retrieve()
                    .toBodilessEntity();

            log.info("Display mode for device '{}' changed to '{}' with edge service", macAddress, displayModePayload);
        } catch (Exception e) {
            log.error(
                    "Failed to change display mode for device '{}' with edge service. baseUrl={}, endpoint={}, error={}",
                    macAddress,
                    settings.baseUrl(),
                    settings.deviceDisplayModeUrl(),
                    e.getMessage()
            );
        }
    }

    private static Map<String, Object> buildThresholdBody(
            String assignedBatchId,
            Double minStock,
            Double maxStock,
            Double minTemperatureCelsius,
            Double maxTemperatureCelsius,
            Double minHumidityPercentage,
            Double maxHumidityPercentage,
            Double anomalyThreshold
    ) {
        var body = new HashMap<String, Object>();
        body.put("assigned_batch_id", assignedBatchId);
        body.put("custom_supply_unit_measurement", CONFIGURATION_PLACEHOLDER_UNIT_MEASUREMENT);
        body.put("minimum_stock", minStock);
        body.put("maximum_stock", maxStock);
        putIfPresent(body, "minimum_temperature_in_celsius", minTemperatureCelsius);
        putIfPresent(body, "maximum_temperature_in_celsius", maxTemperatureCelsius);
        putIfPresent(body, "minimum_humidity_percentage", minHumidityPercentage);
        putIfPresent(body, "maximum_humidity_percentage", maxHumidityPercentage);
        putIfPresent(body, "anomaly_threshold", anomalyThreshold);
        return body;
    }

    private static void putIfPresent(Map<String, Object> body, String key, Object value) {
        if (value != null) {
            body.put(key, value);
        }
    }
}
