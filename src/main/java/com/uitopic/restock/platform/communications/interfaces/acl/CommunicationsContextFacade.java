package com.uitopic.restock.platform.communications.interfaces.acl;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.EmailContents;

/**
 * Facade for the communications bounded context, providing methods to interact with communication-related services.
 * This interface abstracts the underlying implementation details of the communications context, allowing other bounded contexts to interact with it without needing to know about its internal workings.
 */
public interface CommunicationsContextFacade {

    /**
     * Creates a notification based on the provided email contents. This method is responsible for generating a notification that can be sent to users or other systems, based on the information contained in the EmailContents object.
     *
     * @param contents The contents of the email, including subject, body, recipient information, and any other relevant details needed to create a notification.
     * @return A string representing the result of the notification creation process, which could be a success message, an identifier for the created notification, or any relevant information about the outcome of the operation.
     */
    String createNotification(EmailContents contents);
}
