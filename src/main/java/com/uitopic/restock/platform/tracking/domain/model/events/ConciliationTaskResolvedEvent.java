package com.uitopic.restock.platform.tracking.domain.model.events;

import com.uitopic.restock.platform.shared.domain.model.events.NotificationEvent;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.BatchId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.BranchId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.DeviceId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.UserId;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.ConciliationResolutionAction;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.ConciliationResolutionReason;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.ConciliationTaskStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

/**
 * Event representing the resolution of a conciliation task.
 * <p>
 * This event is published when a critical inventory discrepancy has been resolved,
 * either manually by an authorized user or automatically by the system.
 * It can be used to trigger notifications, audit logs, or downstream processes
 * related to inventory reconciliation.
 */
@Getter
@Builder
public class ConciliationTaskResolvedEvent implements NotificationEvent {

    /**
     * The unique identifier of the conciliation task that was resolved.
     */
    @NotEmpty
    private String conciliationTaskId;

    /**
     * The unique identifier of the discrepancy associated with the conciliation task.
     */
    @NotEmpty
    private String discrepancyId;

    /**
     * The unique identifier of the stock comparison task that originated the discrepancy.
     */
    @NotEmpty
    private String stockComparisonTaskId;

    /**
     * The account associated with the resolved conciliation task.
     */
    @NotNull
    private AccountId accountId;

    /**
     * The branch where the discrepancy and conciliation task were registered.
     */
    @NotNull
    private BranchId branchId;

    /**
     * The batch associated with the inventory discrepancy.
     */
    @NotNull
    private BatchId batchId;

    /**
     * The device that reported the physical stock involved in the discrepancy.
     */
    @NotNull
    private DeviceId deviceId;

    /**
     * The unique identifier of the custom supply involved in the discrepancy.
     */
    @NotEmpty
    private String customSupplyId;

    /**
     * The display name of the custom supply involved in the discrepancy.
     */
    @NotEmpty
    private String customSupplyName;

    /**
     * The final status of the conciliation task after it has been resolved.
     */
    @NotNull
    private ConciliationTaskStatus conciliationStatus;

    /**
     * The reason that explains why the conciliation task was resolved.
     */
    @NotNull
    private ConciliationResolutionReason resolutionReason;

    /**
     * The action applied to resolve the conciliation task.
     */
    @NotNull
    private ConciliationResolutionAction resolutionAction;

    /**
     * A textual explanation describing the resolution decision.
     */
    @NotEmpty
    private String resolutionJustification;

    /**
     * The user who resolved the conciliation task.
     * <p>
     * This value is null when the task is resolved automatically by the system.
     */
    private UserId resolvedByUserId;

    /**
     * Returns the source identifier of the notification event.
     * <p>
     * In this case, the source is the resolved conciliation task.
     *
     * @return the conciliation task identifier
     */
    @Override
    public String getSourceId() {
        return this.conciliationTaskId;
    }

    /**
     * Returns the alert level name used by the notification system.
     * <p>
     * For this event, the conciliation task status is used to indicate
     * the current state of the resolved task.
     *
     * @return the conciliation task status name
     */
    @Override
    public String getAlertLevelName() {
        return this.conciliationStatus.name();
    }

    /**
     * Returns the notification title shown to the user.
     *
     * @return the notification title
     */
    @Override
    public String notificationTitle() {
        return "Inventory Discrepancy Resolved";
    }

    /**
     * Returns the notification message shown to the user.
     * <p>
     * The message summarizes which supply was affected and the reason
     * used to resolve the discrepancy.
     *
     * @return the notification message
     */
    @Override
    public String notificationMessage() {
        return "The discrepancy for " + this.customSupplyName
                + " was resolved with action "
                + this.resolutionAction.name()
                + " and reason "
                + this.resolutionReason.name()
                + ".";
    }
}
