package com.uitopic.restock.platform.communications.interfaces.acl;

import com.uitopic.restock.platform.shared.domain.model.commands.NotificationCommand;

/**
 * Facade for the communications bounded context, providing methods to interact with communication-related services.
 * This interface abstracts the underlying implementation details of the communications context, allowing other bounded contexts to interact with it without needing to know about its internal workings.
 */
public interface CommunicationsContextFacade {

    /**
     * Creates a notification based on the provided email contents. This method is responsible for generating a notification that can be sent to users or other systems, based on the information contained in the EmailContents object.
     *
     * @param command the command containing the necessary information to create a notification, including the email contents and any additional metadata required for processing the notification
     */
    void createNotification(NotificationCommand command);
}
