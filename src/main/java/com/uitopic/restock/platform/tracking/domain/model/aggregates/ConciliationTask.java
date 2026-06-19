package com.uitopic.restock.platform.tracking.domain.model.aggregates;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.BatchId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.BranchId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.UserId;
import com.uitopic.restock.platform.tracking.domain.exceptions.DiscrepancyResolutionException;
import com.uitopic.restock.platform.tracking.domain.model.entities.Discrepancy;
import com.uitopic.restock.platform.tracking.domain.model.events.ConciliationTaskResolvedEvent;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.ConciliationResolutionAction;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.ConciliationResolutionReason;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.ConciliationTaskStatus;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.DiscrepancyAlertLevel;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.StockRecord;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Aggregate root representing a conciliation task created from a stock discrepancy.
 * <p>
 * This task tracks the operational resolution process of a mismatch between physical stock
 * and system stock. It stores the discrepancy context, the affected inventory information,
 * the resolution status, and the final resolution decision applied either manually by a user
 * or automatically by the system.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ConciliationTask extends Task {

    /**
     * The unique identifier of the discrepancy that originated this conciliation task.
     * This field is set when the task is created and remains unchanged throughout its lifecycle.
     */
    private String discrepancyId;

    /**
     * The unique identifier of the stock comparison task that detected the mismatch.
     * This field links the conciliation task to the original comparison between physical and system stock.
     */
    private String stockComparisonTaskId;

    /**
     * The identifier of the account related to this conciliation task.
     * This field is used to associate the discrepancy resolution with the corresponding account owner.
     */
    private AccountId accountId;

    /**
     * The identifier of the branch where the stock discrepancy was detected.
     * This field helps locate the physical place where the inventory mismatch occurred.
     */
    private BranchId branchId;

    /**
     * The identifier of the batch affected by the stock discrepancy.
     * This field links the conciliation task to the specific inventory batch being reconciled.
     */
    private BatchId batchId;

    /**
     * The unique identifier of the custom supply affected by the discrepancy.
     * This field identifies the inventory item involved in the conciliation process.
     */
    private String customSupplyId;

    /**
     * The display name of the custom supply affected by the discrepancy.
     * This field is used to provide human-readable information in notifications and audit messages.
     */
    private String customSupplyName;

    /**
     * The stock record obtained from the physical count reported by the device.
     * This value represents the stock physically detected during the comparison process.
     */
    private StockRecord physicalStock;

    /**
     * The stock record obtained from the system before the physical comparison.
     * This value represents the digital inventory quantity stored in the platform.
     */
    private StockRecord systemStock;

    /**
     * The justified withdrawn stock used during the comparison.
     * This value represents stock that is physically outside the device but still justified,
     * such as stock moved to a display counter or another controlled location.
     */
    private Double justifiedWithdrawnStockUsed;

    /**
     * The total physical stock considered during the comparison.
     * This value is calculated from the physical stock plus the justified withdrawn stock.
     */
    private Double totalPhysicalStock;

    /**
     * The absolute difference between the system stock and the total physical stock.
     * This value is used to determine the impact of the discrepancy.
     */
    private Double difference;

    /**
     * The alert level assigned to the discrepancy that originated this conciliation task.
     * Only warning and critical discrepancies are allowed to create conciliation tasks.
     */
    private DiscrepancyAlertLevel alertLevel;

    /**
     * The current status of the conciliation task.
     * This field indicates whether the task is pending, manually resolved, or automatically resolved.
     */
    private ConciliationTaskStatus conciliationStatus;

    /**
     * The reason selected to explain why the discrepancy occurred.
     * This value is assigned when the task is resolved.
     */
    private ConciliationResolutionReason resolutionReason;

    /**
     * The action applied to resolve the discrepancy.
     * This value indicates whether the system stock was adjusted, the justified withdrawn stock was updated,
     * the device was recalibrated, or the task was automatically closed.
     */
    private ConciliationResolutionAction resolutionAction;

    /**
     * The textual justification provided for the resolution decision.
     * This field is required for manual resolutions and is also stored for automatic resolutions.
     */
    private String resolutionJustification;

    /**
     * The identifier of the user who manually resolved the conciliation task.
     * This value is null when the task is resolved automatically by the system.
     */
    private UserId resolvedByUserId;

    /**
     * The timestamp when the conciliation task was resolved.
     * This value is assigned when the resolution process is completed.
     */
    private Instant resolvedAt;

    /**
     * Creates a new conciliation task from a discrepancy that requires conciliation.
     * <p>
     * The constructor copies the relevant discrepancy context, including stock records,
     * account information, branch information, batch information, supply information, and alert level.
     * A conciliation task can only be created from a discrepancy with WARNING or CRITICAL risk level.
     *
     * @param discrepancy the discrepancy that originated the conciliation task
     */
    public ConciliationTask(Discrepancy discrepancy) {
        super(discrepancy.getDeviceId());

        if (discrepancy.getId() == null || discrepancy.getId().isBlank()) {
            throw new DiscrepancyResolutionException("Discrepancy ID cannot be null or blank");
        }

        if (discrepancy.getStockComparisonTaskId() == null || discrepancy.getStockComparisonTaskId().isBlank()) {
            throw new DiscrepancyResolutionException("Stock comparison task ID cannot be null or blank");
        }

        if (discrepancy.getRiskLevel() == DiscrepancyAlertLevel.OK) {
            throw new DiscrepancyResolutionException("Only warning or critical discrepancies can create conciliation tasks");
        }

        this.discrepancyId = discrepancy.getId();
        this.stockComparisonTaskId = discrepancy.getStockComparisonTaskId();

        this.accountId = discrepancy.getAccountId();
        this.branchId = discrepancy.getBranchId();
        this.batchId = discrepancy.getBatchId();

        this.customSupplyId = discrepancy.getCustomSupplyId();
        this.customSupplyName = discrepancy.getCustomSupplyName();

        this.physicalStock = discrepancy.getPhysicalStock();
        this.systemStock = discrepancy.getSystemStock();
        this.justifiedWithdrawnStockUsed = discrepancy.getJustifiedWithdrawnStockUsed();
        this.totalPhysicalStock = discrepancy.getTotalPhysicalStock();
        this.difference = discrepancy.getQuantityDifference();

        this.alertLevel = discrepancy.getRiskLevel();
        this.conciliationStatus = ConciliationTaskStatus.PENDING;
    }

    /**
     * Checks whether the conciliation task is still pending resolution.
     *
     * @return true if the task status is PENDING, false otherwise
     */
    public Boolean isPending() {
        return this.conciliationStatus == ConciliationTaskStatus.PENDING;
    }

    /**
     * Resolves the task by confirming that the digital stock must be adjusted
     * to match the calculated total physical stock.
     * <p>
     * This resolution is valid when the discrepancy was caused by waste, spoilage,
     * theft, loss, or unregistered use. It represents a case where the system stock
     * is considered incorrect and must be corrected.
     *
     * @param resolvedByUserId the user resolving the task
     * @param resolutionReason the reason explaining why the digital stock is incorrect
     * @param resolutionJustification the explanation provided by the administrator
     */
    public void resolveByDigitalStockAdjustment(
            UserId resolvedByUserId,
            ConciliationResolutionReason resolutionReason,
            String resolutionJustification
    ) {
        if (resolutionReason != ConciliationResolutionReason.WASTE_OR_SPOILAGE
                && resolutionReason != ConciliationResolutionReason.THEFT_OR_LOSS
                && resolutionReason != ConciliationResolutionReason.UNREGISTERED_USE) {
            throw new DiscrepancyResolutionException("Invalid reason for digital stock adjustment");
        }
        resolveManually(resolvedByUserId, resolutionReason, ConciliationResolutionAction.ADJUST_DIGITAL_STOCK, resolutionJustification);
    }

    /**
     * Resolves the task by updating the justified withdrawn stock value.
     * <p>
     * This resolution is used when the discrepancy was caused by stock that was
     * physically removed from the device for a justified operational reason,
     * such as transfer to a display counter.
     *
     * @param resolvedByUserId the user resolving the task
     * @param newJustifiedWithdrawnStock the updated amount of justified withdrawn stock
     * @param resolutionJustification the explanation provided by the administrator
     */
    public void resolveByJustifiedWithdrawnStockUpdate(
            UserId resolvedByUserId,
            Double newJustifiedWithdrawnStock,
            String resolutionJustification
    ) {
        if (newJustifiedWithdrawnStock == null || newJustifiedWithdrawnStock < 0.0) {
            throw new DiscrepancyResolutionException("New justified withdrawn stock cannot be null or negative");
        }
        this.justifiedWithdrawnStockUsed = newJustifiedWithdrawnStock;
        resolveManually(resolvedByUserId, ConciliationResolutionReason.TRANSFER_OR_DISPLAY, ConciliationResolutionAction.UPDATE_JUSTIFIED_WITHDRAWN_STOCK, resolutionJustification);
    }

    /**
     * Resolves the task by confirming that the discrepancy was caused by
     * a device calibration problem or sensor fault.
     * <p>
     * This resolution does not adjust digital stock directly. Instead, it records
     * that the corrective action is related to recalibrating or checking the device.
     *
     * @param resolvedByUserId the user resolving the task
     * @param resolutionJustification the explanation provided by the administrator
     */
    public void resolveByDeviceRecalibration(
            UserId resolvedByUserId,
            String resolutionJustification
    ) {
        resolveManually(resolvedByUserId, ConciliationResolutionReason.SENSOR_FAULT, ConciliationResolutionAction.RECALIBRATE_DEVICE, resolutionJustification);
    }

    /**
     * Applies the common behavior for manual resolutions after action-specific validation.
     * <p>
     * This method validates that the task is still pending, checks the required manual
     * resolution data, updates the status, and delegates the final resolution process.
     *
     * @param resolvedByUserId the user resolving the task
     * @param resolutionReason the reason used to explain the discrepancy
     * @param resolutionAction the action used to resolve the discrepancy
     * @param resolutionJustification the explanation provided by the administrator
     */
    private void resolveManually(
            UserId resolvedByUserId,
            ConciliationResolutionReason resolutionReason,
            ConciliationResolutionAction resolutionAction,
            String resolutionJustification
    ) {
        validatePendingResolution();
        if (resolvedByUserId == null) {
            throw new DiscrepancyResolutionException("Resolved by user ID cannot be null");
        }
        if (resolutionJustification == null || resolutionJustification.isBlank()) {
            throw new DiscrepancyResolutionException("Resolution justification cannot be null or blank");
        }

        this.conciliationStatus = ConciliationTaskStatus.RESOLVED_MANUALLY;
        finishResolution(resolutionAction, resolutionReason, resolutionJustification, resolvedByUserId);
    }

    /**
     * Resolves the conciliation task automatically when a later stock comparison
     * confirms that the physical stock and system stock match again.
     * <p>
     * This resolution does not require a user because it is performed by the system.
     */
    public void resolveAutomatically() {
        validatePendingResolution();
        this.conciliationStatus = ConciliationTaskStatus.RESOLVED_AUTOMATICALLY;
        finishResolution(
                ConciliationResolutionAction.AUTOMATIC_CLOSE,
                ConciliationResolutionReason.STOCK_NORMALIZED,
                "Stock was automatically normalized after a matching stock comparison.",
                null);
    }

    /**
     * Validates that the conciliation task is still pending before applying a resolution.
     * <p>
     * A task that has already been resolved cannot be resolved again.
     */
    private void validatePendingResolution() {
        if (!isPending()) {
            throw new DiscrepancyResolutionException("Conciliation task is already resolved");
        }
    }

    /**
     * Completes the resolution process by setting final resolution data,
     * marking the task as completed, and registering a domain event.
     * <p>
     * This method centralizes the final resolution behavior for both manual and automatic
     * resolutions. After the task is completed, a {@link ConciliationTaskResolvedEvent}
     * is registered so other parts of the system can react to the resolution.
     *
     * @param resolutionAction the action applied to resolve the task
     * @param resolutionReason the reason used to explain the resolution
     * @param resolutionJustification the explanation stored for the resolution decision
     * @param resolvedByUserId the user resolving the task, or null when resolved automatically
     */
    private void finishResolution(
            ConciliationResolutionAction resolutionAction,
            ConciliationResolutionReason resolutionReason,
            String resolutionJustification,
            UserId resolvedByUserId
    ) {
        if (resolutionReason == null) {
            throw new DiscrepancyResolutionException("Resolution reason cannot be null");
        }
        if (resolutionAction == null) {
            throw new DiscrepancyResolutionException("Resolution action cannot be null");
        }
        if (resolutionJustification == null || resolutionJustification.isBlank()) {
            throw new DiscrepancyResolutionException("Resolution justification cannot be null or blank");
        }

        this.resolutionReason = resolutionReason;
        this.resolutionAction = resolutionAction;
        this.resolutionJustification = resolutionJustification;
        this.resolvedByUserId = resolvedByUserId;
        this.resolvedAt = Instant.now();

        complete();

        registerDomainEvent(ConciliationTaskResolvedEvent.builder()
                .conciliationTaskId(getId())
                .discrepancyId(this.discrepancyId)
                .stockComparisonTaskId(this.stockComparisonTaskId)
                .accountId(this.accountId)
                .branchId(this.branchId)
                .batchId(this.batchId)
                .deviceId(getDeviceId())
                .customSupplyId(this.customSupplyId)
                .customSupplyName(this.customSupplyName)
                .conciliationStatus(this.conciliationStatus)
                .resolutionReason(this.resolutionReason)
                .resolutionAction(this.resolutionAction)
                .resolutionJustification(this.resolutionJustification)
                .resolvedByUserId(this.resolvedByUserId)
                .build());
    }
}
