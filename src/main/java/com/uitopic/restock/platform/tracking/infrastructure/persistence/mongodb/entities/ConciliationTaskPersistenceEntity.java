package com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.entities;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.BatchId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.BranchId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.DeviceId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.UserId;
import com.uitopic.restock.platform.shared.infrastructure.persistence.mongodb.entities.AuditableAbstractPersistenceEntity;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.ConciliationResolutionAction;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.ConciliationResolutionReason;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.ConciliationTaskStatus;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.DiscrepancyAlertLevel;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.StockRecord;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.TaskStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * Persistence entity representing a conciliation task stored in MongoDB.
 * <p>
 * This entity stores the persisted state of a conciliation task created from a
 * critical inventory discrepancy. It contains the task lifecycle information,
 * the related discrepancy and stock comparison identifiers, the inventory
 * snapshot used during conciliation, and the resolution data required for
 * auditing, notifications, and UI display.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Document(collection = "conciliation_tasks")
public class ConciliationTaskPersistenceEntity extends AuditableAbstractPersistenceEntity {

    /**
     * The unique identifier of the discrepancy that originated the conciliation task.
     */
    private String discrepancyId;

    /**
     * The unique identifier of the stock comparison task that detected the discrepancy.
     */
    private String stockComparisonTaskId;

    /**
     * The account associated with the conciliation task.
     */
    private AccountId accountId;

    /**
     * The branch where the inventory discrepancy was detected.
     */
    private BranchId branchId;

    /**
     * The batch affected by the inventory discrepancy.
     */
    private BatchId batchId;

    /**
     * The device that reported the physical stock involved in the discrepancy.
     */
    private DeviceId deviceId;

    /**
     * The generic task lifecycle status.
     * <p>
     * This status represents the common task state inherited from the task model,
     * such as pending or completed.
     */
    private TaskStatus status;

    /**
     * The unique identifier of the custom supply affected by the discrepancy.
     */
    private String customSupplyId;

    /**
     * The display name of the custom supply affected by the discrepancy.
     */
    private String customSupplyName;

    /**
     * The physical stock record captured from the device during the comparison.
     */
    private StockRecord physicalStock;

    /**
     * The system stock record stored in the platform at the time of comparison.
     */
    private StockRecord systemStock;

    /**
     * The justified withdrawn stock used during the comparison.
     * <p>
     * This value represents stock that is physically outside the device but still
     * justified by an operational reason.
     */
    private Double justifiedWithdrawnStockUsed;

    /**
     * The total physical stock considered in the discrepancy calculation.
     * <p>
     * This value is usually calculated from the physical stock plus the justified
     * withdrawn stock.
     */
    private Double totalPhysicalStock;

    /**
     * The absolute difference between the system stock and the total physical stock.
     */
    private Double difference;

    /**
     * The alert level assigned to the discrepancy that originated the conciliation task.
     */
    private DiscrepancyAlertLevel alertLevel;

    /**
     * The specific conciliation status of the task.
     * <p>
     * This status represents whether the conciliation is pending, manually resolved,
     * or automatically resolved.
     */
    private ConciliationTaskStatus conciliationStatus;

    /**
     * The action applied to resolve the conciliation task.
     * <p>
     * Examples include adjusting digital stock, updating justified withdrawn stock,
     * recalibrating the device, or closing the task automatically.
     */
    private ConciliationResolutionAction resolutionAction;

    /**
     * The reason selected to explain why the discrepancy occurred.
     */
    private ConciliationResolutionReason resolutionReason;

    /**
     * The textual justification provided for the resolution decision.
     */
    private String resolutionJustification;

    /**
     * The user who manually resolved the conciliation task.
     * <p>
     * This value is null when the task was resolved automatically by the system.
     */
    private UserId resolvedByUserId;

    /**
     * The timestamp when the conciliation task was resolved.
     */
    private Instant resolvedAt;
}