package com.uitopic.restock.platform.communications.domain.model.aggregates;

import com.uitopic.restock.platform.communications.domain.model.valueobjects.NotificationSeverity;
import com.uitopic.restock.platform.communications.domain.model.valueobjects.NotificationStatus;
import com.uitopic.restock.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Aggregate root representing a notification sent to a user.
 *
 * A notification contains a message, title, and recipient information.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Notification extends AbstractDomainAggregateRoot<Notification> {

    /** Unique identifier for the notification. */
    private String id;

    /** Identifier of the user who is the recipient of the notification. */
    private String recipientId;

    /** Identifier of the source of the notification. */
    private String sourceId;

    /** Content of the notification message. */
    private String message;

    /** Title of the notification. */
    private String title;

    /** Severity level of the notification. */
    private NotificationSeverity severity;

    /** Status of the notification. */
    private NotificationStatus status;

    /** Constructor for creating a new notification. */
    public Notification(
            String recipientId,
            String sourceId,
            String message,
            String title,
            String severity,
            String status
    ) {
        this.recipientId = recipientId;
        this.sourceId = sourceId;
        this.message = message;
        this.title = title;
        this.severity = NotificationSeverity.valueOf(severity);
        this.status = NotificationStatus.valueOf(status);
    }

    public void markAsRead() {
        this.status = NotificationStatus.READ;
    }
}
