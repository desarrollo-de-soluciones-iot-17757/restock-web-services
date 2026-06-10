package com.uitopic.restock.platform.communications.application.acl;

import com.uitopic.restock.platform.communications.interfaces.acl.CommunicationsContextFacade;
import com.uitopic.restock.platform.shared.domain.model.commands.NotificationCommand;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.EmailContents;
import org.springframework.stereotype.Service;

@Service
public class CommunicationsContextFacadeImpl implements CommunicationsContextFacade {

    /**
     * @inheritDocs
     */
    @Override
    public void createNotification(NotificationCommand command) {

    }
}
