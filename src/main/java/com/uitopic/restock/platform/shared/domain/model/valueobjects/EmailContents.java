package com.uitopic.restock.platform.shared.domain.model.valueobjects;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

/**
 * Value object representing the contents of an email notification.
 * This class encapsulates the details of an email notification, including the resource name, resource count, branch name, source type, and timestamp. It is used to create email notifications when certain events occur, such as when inventory falls below minimum stock levels.
 */
@Data
@Builder
public class EmailContents {

    /**
     * The name of the resource that triggered the notification (e.g., "Batch 10232", "Device-1232"). This provides context to the recipient about which resource is involved in the notification event.
     */
    private String resourceName;

    /**
     * The count of the resource that triggered the notification, represented as a string for flexibility in formatting (e.g., "5 units", "10 items").
     */
    private String resourceCount;

    /**
     * The name of the branch associated with the resource, providing additional context about the location or department related to the notification event (e.g., "Main Branch", "Warehouse A"). This helps recipients understand where the issue is occurring.
     */
    private String branchName;

    /**
     * The type of the source that triggered the notification, such as "IOT" or "CLOUD".
     */
    private String sourceType;

    /**
     * The type of notification to send, it can be "email", "push", or "all" for sending both email and push notifications. This field allows for flexibility in how notifications are delivered to the recipients based on their preferences or the nature of the event.
     */
    private String notificationType;

    /**
     * The type of event that triggered the notification, such as "InventoryBelowMinimumStock", "CustomSupplyDeleted", etc. This field helps categorize the notification and allows for different handling or formatting based on the event type.
     */
    private String eventType;

    /**
     * The account ID associated with the notification event, represented as a string. This field is used to identify which account the notification is related to, allowing for personalized notifications and ensuring that the correct recipients receive the notification based on their account association.
     */
    private String accountId;

    /**
     * The timestamp when the notification was created. This is automatically set to the current time when an instance of EmailContents is created, providing a record of when the notification event occurred. This can be useful for logging, auditing, or displaying the time of the event in the notification message.
     */
    private final Instant timestamp = Instant.now();
}
