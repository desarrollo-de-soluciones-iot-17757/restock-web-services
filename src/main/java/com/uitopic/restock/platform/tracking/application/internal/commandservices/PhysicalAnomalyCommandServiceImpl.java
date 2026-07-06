package com.uitopic.restock.platform.tracking.application.internal.commandservices;

import com.uitopic.restock.platform.communications.domain.model.commands.CreateNotificationCommand;
import com.uitopic.restock.platform.communications.domain.model.valueobjects.SourceType;
import com.uitopic.restock.platform.communications.domain.services.NotificationCommandService;
import com.uitopic.restock.platform.devices.domain.model.valueobjects.MacAddress;
import com.uitopic.restock.platform.devices.domain.repositories.DeviceRepository;
import com.uitopic.restock.platform.tracking.domain.model.aggregates.PhysicalAnomaly;
import com.uitopic.restock.platform.tracking.domain.model.commands.CreatePhysicalAnomalyCommand;
import com.uitopic.restock.platform.tracking.domain.repositories.PhysicalAnomalyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation of PhysicalAnomalyCommandService.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PhysicalAnomalyCommandServiceImpl implements PhysicalAnomalyCommandService {

    private final PhysicalAnomalyRepository physicalAnomalyRepository;
    private final DeviceRepository deviceRepository;
    private final NotificationCommandService notificationCommandService;

    @Override
    public PhysicalAnomaly handle(CreatePhysicalAnomalyCommand command) {
        log.info("Handling CreatePhysicalAnomalyCommand for device: {}, value: {}", command.deviceId(), command.registeredValue());

        PhysicalAnomaly anomaly = new PhysicalAnomaly(command);
        PhysicalAnomaly savedAnomaly = physicalAnomalyRepository.save(anomaly);

        try {
            String recipientId = resolveRecipientId(command.deviceId());
            notificationCommandService.handle(new CreateNotificationCommand(
                    recipientId,
                    command.deviceId(),
                    SourceType.DISCREPANCY,
                    "Scale Sensor Variance Alert",
                    "Physical weight anomaly of " + command.registeredValue() + "g detected on device " + command.deviceId(),
                    "HIGH"
            ));
        } catch (Exception e) {
            log.warn("Could not dispatch notification for physical anomaly: {}", e.getMessage());
        }

        return savedAnomaly;
    }

    private String resolveRecipientId(String deviceId) {
        if (deviceId == null) return "acc-123";
        return deviceRepository.findByMacAddress(new MacAddress(deviceId))
                .or(() -> deviceRepository.findById(deviceId))
                .map(d -> d.getAccountId() != null ? d.getAccountId().getAccountId() : "acc-123")
                .orElse("acc-123");
    }
}
