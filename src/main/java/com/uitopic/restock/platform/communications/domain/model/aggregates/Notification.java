package com.uitopic.restock.platform.communications.domain.model.aggregates;

import com.uitopic.restock.platform.communications.domain.model.valueobjects.NotificationSeverity;
import com.uitopic.restock.platform.communications.domain.model.valueobjects.NotificationStatus;
import com.uitopic.restock.platform.communications.domain.model.valueobjects.SourceType;
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

    /** Optional identifier of the source entity that triggered the notification (e.g., device ID, batch ID). */
    private String sourceId;

    /** Type of the source that triggered the notification (e.g., DEVICE, BATCH, INVENTORY). */
    private SourceType sourceType;

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
            SourceType sourceType,
            String message,
            String title,
            String severity
    ) {
        this.recipientId = recipientId;
        this.sourceId = sourceId;
        this.sourceType = sourceType;
        this.message = message;
        this.title = title;
        this.severity = NotificationSeverity.valueOf(severity);
        this.status = NotificationStatus.UNREAD;
    }

    /**
     * Marks the notification as read by updating its status.
     */
    public void markAsRead() {
        this.status = NotificationStatus.READ;
    }
}
