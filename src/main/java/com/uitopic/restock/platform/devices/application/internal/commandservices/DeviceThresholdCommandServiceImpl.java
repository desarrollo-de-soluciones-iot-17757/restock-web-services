package com.uitopic.restock.platform.devices.application.internal.commandservices;

import com.uitopic.restock.platform.devices.domain.model.commands.CreateDeviceThresholdCommand;
import com.uitopic.restock.platform.devices.domain.model.entities.DeviceThreshold;
import com.uitopic.restock.platform.devices.domain.model.valueobjects.Humidity;
import com.uitopic.restock.platform.devices.domain.model.valueobjects.Temperature;
import com.uitopic.restock.platform.devices.domain.repositories.DeviceThresholdRepository;
import com.uitopic.restock.platform.devices.domain.services.DeviceThresholdCommandService;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeviceThresholdCommandServiceImpl implements DeviceThresholdCommandService {

    private final DeviceThresholdRepository thresholdRepository;

    public DeviceThresholdCommandServiceImpl(DeviceThresholdRepository thresholdRepository) {
        this.thresholdRepository = thresholdRepository;
    }

    @Override
    public DeviceThreshold handle(CreateDeviceThresholdCommand command) {
        log.info("Creating device threshold for customSupplyId='{}' and accountId='{}'",
                command.customSupplyId(), command.accountId());

        Temperature temperature = null;
        if (command.minTemperatureCelsius() != null && command.maxTemperatureCelsius() != null) {
            temperature = new Temperature(command.minTemperatureCelsius(), command.maxTemperatureCelsius());
        }

        Humidity humidity = null;
        if (command.minHumidityPercentage() != null && command.maxHumidityPercentage() != null) {
            humidity = new Humidity(command.minHumidityPercentage(), command.maxHumidityPercentage());
        }

        var threshold = new DeviceThreshold(
                new AccountId(command.accountId()),
                command.customSupplyId(),
                command.minStock(),
                command.maxStock(),
                command.anomalyThreshold(),
                temperature,
                humidity
        );

        var saved = thresholdRepository.save(threshold);
        log.info("Device threshold created successfully: id='{}'", saved.getId());
        return saved;
    }
}
