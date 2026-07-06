package com.uitopic.restock.platform.devices.application.internal.eventhandler;

import com.uitopic.restock.platform.devices.application.internal.outboundservices.edgeservice.EdgeService;
import com.uitopic.restock.platform.devices.domain.model.events.DeviceConfiguredEvent;
import com.uitopic.restock.platform.resources.application.internal.outboundservices.acl.ExternalCommunicationsService;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DeviceConfiguredEventHandler {

    private final EdgeService edgeService;
    private final ExternalCommunicationsService externalCommunicationsService;

    public DeviceConfiguredEventHandler(
            EdgeService edgeService,
            ExternalCommunicationsService externalCommunicationsService
    ) {
        this.edgeService = edgeService;
        this.externalCommunicationsService = externalCommunicationsService;
    }

    @EventListener
    public void on(DeviceConfiguredEvent event) {
        log.info("Handling DeviceConfiguredEvent for device with MAC address='{}'", event.getMacAddress());
        var threshold = event.getDeviceThreshold();
        var temperature = threshold.getTemperature();
        var humidity = threshold.getHumidity();
        edgeService.configureDevice(
                event.getMacAddress(),
                event.getAssignedBatchId(),
                threshold.getMinStock(),
                threshold.getMaxStock(),
                temperature != null ? temperature.minCelsius() : null,
                temperature != null ? temperature.maxCelsius() : null,
                humidity != null ? humidity.minPercentage() : null,
                humidity != null ? humidity.maxPercentage() : null,
                threshold.getAnomalyThreshold()
        );
        externalCommunicationsService.createNotification(event, new AccountId(event.getAccountId()));
    }
}
