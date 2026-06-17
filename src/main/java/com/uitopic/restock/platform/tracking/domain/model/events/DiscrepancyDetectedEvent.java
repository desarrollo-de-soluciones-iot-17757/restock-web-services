package com.uitopic.restock.platform.tracking.domain.model.events;

import com.uitopic.restock.platform.shared.domain.model.events.NotificationEvent;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.DeviceId;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.DiscrepancyAlertLevel;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.StockRecord;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

/**
 * Event representing the detection of a discrepancy in inventory tracking. This event captures the details of the physical and system stock records, as well as the associated device ID. It is used to notify the system of a detected discrepancy, allowing for further processing and resolution.
 */
@Getter
@Builder
public class DiscrepancyDetectedEvent implements NotificationEvent {

    /**
     * The custom supply name associated with the discrepancy, provided by the comparison task. This is used to identify the specific supply item that has a discrepancy between the physical and system stock records.
     */
    @NotEmpty
    private String customSupplyName;

    /**
     * The physical stock record, provided by the comparison task. This represents the actual inventory level detected during the physical count.
     */
    @NotNull
    private Double physicalStock;

    /**
     * The system stock record, provided by the comparison task. This represents the inventory level recorded in the system before the physical count was conducted.
     */
    @NotNull
    private Double systemStock;

    /**
     * The threshold used to determine the risk level of the discrepancy, provided by the comparison task. This value is used to evaluate the severity of the discrepancy based on predefined thresholds for quantity differences.
     */
    @NotEmpty
    private Double thresholdUsed;

    /**
     * The device ID associated with the discrepancy, provided by the request. This is used to track the source of the discrepancy and can be helpful for auditing and resolution purposes.
     */
    @NotNull
    private DeviceId deviceId;

    /**
     * The account ID associated with the discrepancy, provided by the request. This is used to identify the account responsible for the inventory and can be helpful for auditing and resolution purposes.
     */
    @NotNull
    private AccountId accountId;

    /**
     * The alert level indicating the severity of the discrepancy, provided by the comparison task. This value is used to categorize the discrepancy and determine the appropriate actions to take based on its severity (e.g., restocking, sending notifications).
     */
    @NotNull
    private DiscrepancyAlertLevel alertLevel;

    /**
     * Returns the source ID of the event, which is the device ID associated with the discrepancy. This allows the system to identify the origin of the event and can be useful for tracking and auditing purposes.
     *
     * @return a string representing the device ID that is the source of the event
     */
    @Override
    public String getSourceId() {
        return this.deviceId.getDeviceId();
    }

    /**
     * Returns the alert level indicating the severity of the event, which can be used to determine the appropriate actions to take (e.g., restocking, sending notifications). This field is essential for categorizing the notification event and ensuring that the right level of attention is given to the issue based on its severity.
     *
     * @return a string representing the alert level (e.g., "LOW", "MEDIUM", "HIGH") that indicates the severity of the event
     */
    @Override
    public String getAlertLevelName() {
        return this.alertLevel.name();
    }

    /**
     * Returns the title of the notification to be displayed to the user when a discrepancy is detected. This title provides a concise summary of the event, indicating that an inventory discrepancy has been identified.
     *
     * @return a short, descriptive title summarizing the notification (e.g., "Inventory Discrepancy Detected").
     */
    @Override
    public String notificationTitle() {
        return "Inventory Discrepancy Detected";
    }

    /**
     * Returns the full message body of the notification to be displayed to the user when a discrepancy is detected. This message provides detailed information about the event, indicating that a difference has been detected between the physical stock and the recorded stock, prompting the user to take appropriate action to investigate and resolve the discrepancy.
     *
     * @return a human-readable message with the details of the event (e.g., "A difference is detected between the physical stock and the recorded stock.").
     */
    @Override
    public String notificationMessage() {
        return "A difference is detected between the physical stock and the recorded stock.";
    }
}
