package com.uitopic.restock.platform.communications.infrastructure.persistence.mongodb.entities;

import com.uitopic.restock.platform.communications.domain.model.valueobjects.NotificationSeverity;
import com.uitopic.restock.platform.communications.domain.model.valueobjects.NotificationStatus;
import com.uitopic.restock.platform.shared.infrastructure.persistence.mongodb.entities.AuditableAbstractPersistenceEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Persistence entity representing a notification stored in MongoDB.
 * This class is used to map the Notification domain aggregate to a MongoDB document for persistence.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Document(collection = "notifications")
public class NotificationPersistenceEntity extends AuditableAbstractPersistenceEntity {

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
}
