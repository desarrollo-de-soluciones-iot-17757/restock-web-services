package com.uitopic.restock.platform.tracking.domain.model.events;

import com.uitopic.restock.platform.shared.domain.model.events.NotificationEvent;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.DeviceId;
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
    private StockRecord physicalStock;

    /**
     * The system stock record, provided by the comparison task. This represents the inventory level recorded in the system before the physical count was conducted.
     */
    @NotNull
    private StockRecord systemStock;

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
}
