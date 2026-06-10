package com.uitopic.restock.platform.communications.domain.model.aggregates;

import com.uitopic.restock.platform.communications.domain.model.valueobjects.ClientPlatform;
import com.uitopic.restock.platform.communications.domain.model.valueobjects.NotificationProvider;
import com.uitopic.restock.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Represents a push notification subscription for a user. This aggregate root manages the state of a user's
 * push notification subscription, including the provider token, platform, and notification provider.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PushSubscription extends AbstractDomainAggregateRoot<PushSubscription> {

    /** Unique identifier for the push subscription. */
    private String id;

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

    /** Constructor for creating a new push subscription. */
    public PushSubscription(
            String userId,
            String providerToken,
            String platform,
            String provider
    ) {
        this.userId = userId;
        this.providerToken = providerToken;
        this.platform = ClientPlatform.valueOf(platform);
        this.provider = NotificationProvider.valueOf(provider);
        this.active = true;
    }

    /** Deactivates the push subscription. */
    public void deactivate() {
        this.active = false;
    }

    /** Refreshes the push subscription token. */
    public void refreshToken(String newToken) {
        this.providerToken = newToken;
    }
}
