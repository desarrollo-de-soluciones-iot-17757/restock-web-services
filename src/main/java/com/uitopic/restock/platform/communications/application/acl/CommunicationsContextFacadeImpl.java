package com.uitopic.restock.platform.communications.application.acl;

import com.uitopic.restock.platform.communications.domain.model.commands.CreateNotificationCommand;
import com.uitopic.restock.platform.communications.domain.model.commands.SendPushNotificationCommand;
import com.uitopic.restock.platform.communications.domain.model.valueobjects.NotificationSeverity;
import com.uitopic.restock.platform.communications.domain.services.NotificationCommandService;
import com.uitopic.restock.platform.communications.interfaces.acl.CommunicationsContextFacade;
import com.uitopic.restock.platform.shared.domain.model.commands.NotificationCommand;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.NotificationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation of the CommunicationsContextFacade, providing methods to interact with communication-related services.
 * This class serves as a facade for the communications bounded context, allowing other bounded contexts to interact with it without needing to know about its internal workings.
 */
@Service
@Slf4j
public class CommunicationsContextFacadeImpl implements CommunicationsContextFacade {

    /** Service for handling notification commands. */
    private final NotificationCommandService notificationCommandService;

    /** Constructs a CommunicationsContextFacadeImpl with the required services. */
    public CommunicationsContextFacadeImpl(NotificationCommandService notificationCommandService) {
        this.notificationCommandService = notificationCommandService;
    }

    /**
     * Processes a notification command by sending a push notification when the notification type allows it.
     *
     * @param command the notification command containing the account ID, notification type, and event payload
     */
    @Override
    public void processNotification(NotificationCommand command) {
        var recipientUserId = command.accountId().getAccountId();
        var sourceId = command.event().getClass().getSimpleName();
        var title = "";
        var message = "";
        var severity = NotificationSeverity.INFO.name();

        log.info("Processing notification push. recipientUserId={}, notificationType={}, event={}",
                recipientUserId,
                command.notificationType(),
                command.event().getClass().getSimpleName());

        if (shouldSendPushNotification(command.notificationType())) {
            notificationCommandService.handle(new CreateNotificationCommand(
                    recipientUserId,
                    sourceId,
                    title,
                    message,
                    severity
            ));

            notificationCommandService.handle(new SendPushNotificationCommand(
                    recipientUserId,
                    sourceId,
                    title,
                    message,
                    severity
            ));
        }

    }

    /** Determines whether a push notification should be sent for the requested notification type. */
    private boolean shouldSendPushNotification(NotificationType notificationType) {
        return notificationType == NotificationType.ALL || notificationType == NotificationType.PUSH;
    }
}
