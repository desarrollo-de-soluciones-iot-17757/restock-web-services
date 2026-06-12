package com.uitopic.restock.platform.resources.application.internal.outboundservices.acl;

import com.uitopic.restock.platform.communications.interfaces.acl.CommunicationsContextFacade;
import com.uitopic.restock.platform.shared.domain.model.commands.NotificationCommand;
import com.uitopic.restock.platform.shared.domain.model.events.NotificationEvent;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.stereotype.Service;

/**
 * This service is responsible for interacting with the communications context, allowing the resources application to send notifications or emails related to resources without being tightly coupled to the communications context's implementation.
 */
@Service(value = "ResourcesExternalCommunicationsService")
public class ExternalCommunicationsService {

    /** The CommunicationsContextFacade is injected into this service, allowing it to interact with the communications context without needing to know about its internal workings. This promotes loose coupling between the resources application and the communications context. */
    private final CommunicationsContextFacade communicationsContextFacade;

    /** Constructs an ExternalCommunicationsService with the required CommunicationsContextFacade. This constructor is used for dependency injection, allowing the communications context to be injected into this service when it is created. */
    public ExternalCommunicationsService(CommunicationsContextFacade communicationsContextFacade) {
        this.communicationsContextFacade = communicationsContextFacade;
    }

    /** This method creates a notification by constructing a NotificationCommand with the provided event and account ID, and then processing it through the CommunicationsContextFacade. The notification type is set to ALL, indicating that the notification should be delivered through all available channels (e.g., in-app, email, push). This method allows the resources application to send notifications related to resources without needing to know about the specific implementation details of how notifications are created and delivered in the communications context. */
    public void createNotification(NotificationEvent event, AccountId accountId) {
        var command = new NotificationCommand(accountId, event);
        communicationsContextFacade.processNotification(command);
    }
}
