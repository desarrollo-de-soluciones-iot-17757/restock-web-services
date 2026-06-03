package com.uitopic.restock.platform.resources.application.internal.outboundservices.acl;

import com.uitopic.restock.platform.communications.interfaces.acl.CommunicationsContextFacade;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.EmailContents;
import org.springframework.stereotype.Service;

/**
 * This service is responsible for interacting with the communications context, allowing the resources application to send notifications or emails related to resources without being tightly coupled to the communications context's implementation.
 */
@Service
public class ExternalCommunicationsService {

    // This service acts as an adapter to the communications context, allowing the resources application to interact with it without being tightly coupled to its implementation.
    private final CommunicationsContextFacade communicationsContextFacade;

    // This service can be used to interact with the communications context, for example to send notifications or emails related to resources.
    public ExternalCommunicationsService(CommunicationsContextFacade communicationsContextFacade) {
        this.communicationsContextFacade = communicationsContextFacade;
    }

    /**
     * Sends a notification to the communications context with the provided email contents. This method can be used to trigger notifications or emails based on certain events or conditions related to resources, such as when inventory falls below minimum stock levels or when a custom supply is deleted.
     *
     * @param contents The contents of the email notification, encapsulated in an EmailContents object, which includes details such as the resource name, resource count, branch name, source type, notification type, event type, account ID, and timestamp.
     */
    void sendNotificationToCommunicationsContext(EmailContents contents) {
        // This method can be used to send a notification to the communications context, allowing the resources application to trigger notifications or emails based on certain events or conditions related to resources.
        communicationsContextFacade.createNotification(contents);
    }
}
