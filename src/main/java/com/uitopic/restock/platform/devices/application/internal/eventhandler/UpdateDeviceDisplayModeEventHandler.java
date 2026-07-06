package com.uitopic.restock.platform.devices.application.internal.eventhandler;

import com.uitopic.restock.platform.devices.application.internal.outboundservices.edgeservice.EdgeService;
import com.uitopic.restock.platform.devices.domain.model.events.UpdateDeviceDisplayModeEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UpdateDeviceDisplayModeEventHandler {

    private final EdgeService edgeService;

    public UpdateDeviceDisplayModeEventHandler(
            EdgeService edgeService
    ) {
        this.edgeService = edgeService;
    }

    @EventListener
    public void on(UpdateDeviceDisplayModeEvent event) {
        edgeService.changeDisplayMode(
                event.getMacAddress(),
                event.getDisplayMode()
        );
    }
}
