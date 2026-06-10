package com.uitopic.restock.platform.communications.interfaces.acl;

import com.uitopic.restock.platform.shared.domain.model.commands.NotificationCommand;

/**
 * Facade for the communications bounded context, providing methods to interact with communication-related services.
 * This interface abstracts the underlying implementation details of the communications context, allowing other bounded contexts to interact with it without needing to know about its internal workings.
 */
public interface CommunicationsContextFacade {

    /**
     * Processes a notification event according to its delivery type.
     *
     * @param command command containing the account, delivery type, and event payload
     */
    void processNotification(NotificationCommand command);
}
