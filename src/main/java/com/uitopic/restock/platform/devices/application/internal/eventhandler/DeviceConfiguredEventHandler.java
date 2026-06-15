package com.uitopic.restock.platform.devices.application.internal.eventhandler;

import com.uitopic.restock.platform.devices.application.internal.outboundservices.edgeservice.EdgeService;
import com.uitopic.restock.platform.devices.domain.model.events.DeviceConfiguredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DeviceConfiguredEventHandler {

    private final EdgeService edgeService;

    public DeviceConfiguredEventHandler(EdgeService edgeService) {
        this.edgeService = edgeService;
    }

    @EventListener
    public void on(DeviceConfiguredEvent event) {
        log.info("Handling DeviceConfiguredEvent for device with MAC address='{}'", event.getMacAddress());
        edgeService.registerDevice(event.getMacAddress());
    }
}
