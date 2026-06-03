package com.uitopic.restock.platform.communications.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "NotificationWrapperResource",
        description = "A wrapper resource for notifications, encapsulating the details of a notification."
)
public record NotificationWrapperResource() {
}
