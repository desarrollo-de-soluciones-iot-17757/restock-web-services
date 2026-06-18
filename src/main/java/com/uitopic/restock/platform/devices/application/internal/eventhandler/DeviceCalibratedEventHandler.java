package com.uitopic.restock.platform.devices.application.internal.eventhandler;

import com.uitopic.restock.platform.devices.application.internal.outboundservices.edgeservice.EdgeService;
import com.uitopic.restock.platform.devices.domain.model.events.DeviceCalibratedEvent;
import com.uitopic.restock.platform.resources.application.internal.outboundservices.acl.ExternalCommunicationsService;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DeviceCalibratedEventHandler {

    private final EdgeService edgeService;
    private final ExternalCommunicationsService externalCommunicationsService;

    public DeviceCalibratedEventHandler(
            EdgeService edgeService,
            ExternalCommunicationsService externalCommunicationsService
    ) {
        this.edgeService = edgeService;
        this.externalCommunicationsService = externalCommunicationsService;
    }

    @EventListener
    public void on(DeviceCalibratedEvent event) {
        log.info("Handling DeviceCalibratedEvent for device with MAC address='{}'", event.getMacAddress());
        var threshold = event.getDeviceThreshold();
        var temperature = threshold.getTemperature();
        var humidity = threshold.getHumidity();
        edgeService.calibrateDevice(
                event.getMacAddress(),
                event.getAssignedBatchId(),
                threshold.getMinStock(),
                threshold.getMaxStock(),
                temperature != null ? temperature.minCelsius() : null,
                temperature != null ? temperature.maxCelsius() : null,
                humidity != null ? humidity.minPercentage() : null,
                humidity != null ? humidity.maxPercentage() : null,
                event.getWeightMeasurement()
        );
        externalCommunicationsService.createNotification(event, new AccountId(event.getAccountId()));
    }
}
