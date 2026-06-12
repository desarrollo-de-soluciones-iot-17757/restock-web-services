package com.uitopic.restock.platform.shared.domain.model.commands;

import com.uitopic.restock.platform.shared.domain.model.events.NotificationEvent;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.NotificationType;

/**
 * Command representing a request to send a notification.
 *
 * @param accountId the account ID associated with the notification event, represented as a value object. This field is used to identify which account the notification is related to, allowing for personalized notifications and ensuring that the correct recipients receive the notification based on their account association.
 * @param event the type of event that triggered the notification, represented as a value object. This field helps categorize the notification and allows for different handling or formatting based on the event type, such as "InventoryBelowMinimumStock", "CustomSupplyDeleted", etc.
 */
public record NotificationCommand(
        AccountId accountId,
        NotificationEvent event
) {
}
