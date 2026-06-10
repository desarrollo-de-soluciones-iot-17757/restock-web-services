package com.uitopic.restock.platform.tracking.application.internal.outboundservices.acl;

import com.uitopic.restock.platform.communications.interfaces.acl.CommunicationsContextFacade;
import com.uitopic.restock.platform.shared.domain.model.commands.NotificationCommand;
import com.uitopic.restock.platform.shared.domain.model.events.NotificationEvent;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service responsible for handling communication-related operations for the tracking bounded context. This service interacts with the CommunicationsContextFacade to create notifications based on events that occur within the tracking context. By using the facade, this service can delegate communication-related tasks without needing to know about the internal workings of the communications context, allowing for a clean separation of concerns between the two bounded contexts.
 */
@Service(value = "trackingExternalCommunicationsService")
@RequiredArgsConstructor
public class ExternalCommunicationsService {

    // Facade for the communications bounded context, allowing this service to interact with communication-related services without needing to know about their internal workings. This facade provides methods to create notifications and perform other communication-related operations.
    private final CommunicationsContextFacade communicationsContextFacade;

    /**
     * Creates a notification based on the provided event and account ID. This method constructs a NotificationCommand using the given event and account ID, and then delegates the creation of the notification to the CommunicationsContextFacade. The notification type is set to ALL, indicating that the notification should be sent to all relevant recipients.
     *
     * @param event the event containing the information to be included in the notification, such as the message, timestamp, and any additional data relevant to the notification
     * @param accountId the account ID associated with the notification, which can be used to determine the recipients of the notification and any additional context needed for processing the notification
     */
    public void createNotification(NotificationEvent event, AccountId accountId) {
        var command = new NotificationCommand(accountId, NotificationType.ALL, event);
        communicationsContextFacade.processNotification(command);
    }
}
