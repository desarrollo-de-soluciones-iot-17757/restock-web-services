package com.uitopic.restock.platform.communications.infrastructure.persistence.mongodb.entities;

import com.uitopic.restock.platform.communications.domain.model.valueobjects.ClientPlatform;
import com.uitopic.restock.platform.communications.domain.model.valueobjects.NotificationProvider;
import com.uitopic.restock.platform.shared.infrastructure.persistence.mongodb.entities.AuditableAbstractPersistenceEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Persistence entity representing a push subscription stored in MongoDB.
 * This class is used to map the PushSubscription domain aggregate to a MongoDB document for persistence.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Document(collection = "push_subscriptions")
public class PushSubscriptionPersistenceEntity extends AuditableAbstractPersistenceEntity {

    /** Unique identifier for the push subscription. */
    private String userId;

    /** Token provided by the push notification provider. */
    private String providerToken;

    /** Platform on which the push notification is being sent. */
    private ClientPlatform platform;

    /** Notification provider used for sending push notifications. */
    private NotificationProvider provider;

    /** Flag indicating whether the push subscription is active. */
    private boolean active;
}
