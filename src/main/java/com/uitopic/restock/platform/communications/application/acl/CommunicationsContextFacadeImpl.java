package com.uitopic.restock.platform.communications.application.acl;

import com.uitopic.restock.platform.communications.interfaces.acl.CommunicationsContextFacade;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.EmailContents;
import org.springframework.stereotype.Service;

@Service
public class CommunicationsContextFacadeImpl implements CommunicationsContextFacade {

    /**
     * Creates a notification based on the provided email contents. This method is responsible for generating a notification that can be sent to users or other systems, based on the information contained in the EmailContents object.
     *
     * @param contents The contents of the email, including subject, body, recipient information, and any other relevant details needed to create a notification.
     */
    @Override
    public void createNotification(EmailContents contents) {

    }
}
