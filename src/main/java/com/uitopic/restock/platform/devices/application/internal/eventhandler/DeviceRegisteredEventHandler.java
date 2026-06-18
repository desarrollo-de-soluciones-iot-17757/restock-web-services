package com.uitopic.restock.platform.devices.application.internal.eventhandler;

import com.uitopic.restock.platform.devices.application.internal.outboundservices.edgeservice.EdgeService;
import com.uitopic.restock.platform.devices.domain.model.events.DeviceRegisteredEvent;
import com.uitopic.restock.platform.resources.application.internal.outboundservices.acl.ExternalCommunicationsService;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DeviceRegisteredEventHandler {

    private final EdgeService edgeService;
    private final ExternalCommunicationsService externalCommunicationsService;

    public DeviceRegisteredEventHandler(
            EdgeService edgeService,
            ExternalCommunicationsService externalCommunicationsService
    ) {
        this.edgeService = edgeService;
        this.externalCommunicationsService = externalCommunicationsService;
    }

    @EventListener
    public void on(DeviceRegisteredEvent event) {
        log.info("Handling DeviceRegisteredEvent for device with MAC address='{}'", event.getMacAddress());
        edgeService.registerDevice(event.getMacAddress(), event.getDeviceToken());
        externalCommunicationsService.createNotification(event, new AccountId(event.getAccountId()));
    }
}
