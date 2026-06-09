package com.uitopic.restock.platform.devices.application.internal.commandservices;

import com.uitopic.restock.platform.devices.domain.model.commands.CreateDeviceThresholdCommand;
import com.uitopic.restock.platform.devices.domain.model.entities.DeviceThreshold;
import com.uitopic.restock.platform.devices.domain.model.valueobjects.Humidity;
import com.uitopic.restock.platform.devices.domain.model.valueobjects.Temperature;
import com.uitopic.restock.platform.devices.domain.repositories.DeviceThresholdRepository;
import com.uitopic.restock.platform.devices.domain.services.DeviceThresholdCommandService;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.DeviceId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

/**
 * Implementation of the DeviceThresholdCommandService that handles commands related to device thresholds.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceThresholdCommandServiceImpl implements DeviceThresholdCommandService {

    // Repository for managing device thresholds
    private final DeviceThresholdRepository thresholdRepository;

    /**
     * @inheritDocs
     */
    @Override
    public DeviceThreshold handle(CreateDeviceThresholdCommand command) {
        log.info("Creating device threshold for customSupplyId='{}' and accountId='{}'",
                command.customSupplyId(), command.accountId());

        Temperature temperature = null;
        if (command.minTemperatureCelsius() != null && command.maxTemperatureCelsius() != null) {
            temperature = new Temperature(command.minTemperatureCelsius(), command.maxTemperatureCelsius());
        }

        var threshold = getDeviceThreshold(command, temperature);

        var saved = thresholdRepository.save(threshold);
        log.info("Device threshold created successfully: id='{}'", saved.getId());
        return saved;
    }

    /**
     * Helper method to create a DeviceThreshold entity from the command and temperature.
     *
     * @param command the command containing the threshold details
     * @param temperature the temperature value object, which may be null if temperature thresholds are not provided
     * @return a DeviceThreshold entity constructed from the command and temperature
     */
    private static @NonNull DeviceThreshold getDeviceThreshold(CreateDeviceThresholdCommand command, Temperature temperature) {
        Humidity humidity = null;
        if (command.minHumidityPercentage() != null && command.maxHumidityPercentage() != null) {
            humidity = new Humidity(command.minHumidityPercentage(), command.maxHumidityPercentage());
        }

        return new DeviceThreshold(
                new AccountId(command.accountId()),
                new DeviceId(command.deviceId()),
                command.customSupplyId(),
                command.minStock(),
                command.maxStock(),
                command.anomalyThreshold(),
                temperature,
                humidity
        );
    }
}
