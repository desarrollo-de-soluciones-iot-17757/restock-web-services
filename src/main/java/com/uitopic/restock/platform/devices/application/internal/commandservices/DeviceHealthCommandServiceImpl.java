package com.uitopic.restock.platform.devices.application.internal.commandservices;

import com.uitopic.restock.platform.communications.domain.model.commands.CreateNotificationCommand;
import com.uitopic.restock.platform.communications.domain.model.valueobjects.SourceType;
import com.uitopic.restock.platform.communications.domain.services.NotificationCommandService;
import com.uitopic.restock.platform.devices.domain.model.aggregates.DeviceHealth;
import com.uitopic.restock.platform.devices.domain.model.commands.RegisterDeviceHealthCommand;
import com.uitopic.restock.platform.devices.domain.model.valueobjects.MacAddress;
import com.uitopic.restock.platform.devices.domain.repositories.DeviceHealthRepository;
import com.uitopic.restock.platform.devices.domain.repositories.DeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation of DeviceHealthCommandService.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceHealthCommandServiceImpl implements DeviceHealthCommandService {

    private final DeviceHealthRepository deviceHealthRepository;
    private final DeviceRepository deviceRepository;
    private final NotificationCommandService notificationCommandService;

    @Override
    public DeviceHealth handle(RegisterDeviceHealthCommand command) {
        log.info("Handling RegisterDeviceHealthCommand for device: {}, metric: {}, alertType: {}",
                command.deviceId(), command.metric(), command.alertType());

        var deviceHealth = new DeviceHealth(command);
        var savedHealth = deviceHealthRepository.save(deviceHealth);

        try {
            String recipientId = resolveRecipientId(command.deviceId());
            String metricName = command.metric() != null ? command.metric() : "Device";
            String title = "Microcontroller Health Alert: " + metricName;
            String msg = command.message() != null ? command.message() : ("Hardware anomaly (" + metricName + ") detected on device " + command.deviceId());
            if (command.value() != null && command.threshold() != null) {
                msg += " (Reading: " + command.value() + " | Limit: " + command.threshold() + ")";
            }
            notificationCommandService.handle(new CreateNotificationCommand(
                    recipientId,
                    command.deviceId(),
                    SourceType.DEVICE,
                    title,
                    msg,
                    "WARNING"
            ));
        } catch (Exception e) {
            log.warn("Could not dispatch notification for device health anomaly: {}", e.getMessage());
        }

        return savedHealth;
    }

    private String resolveRecipientId(String deviceId) {
        if (deviceId == null) return "acc-123";
        return deviceRepository.findByMacAddress(new MacAddress(deviceId))
                .or(() -> deviceRepository.findById(deviceId))
                .map(d -> d.getAccountId() != null ? d.getAccountId().getAccountId() : "acc-123")
                .orElse("acc-123");
    }
}
