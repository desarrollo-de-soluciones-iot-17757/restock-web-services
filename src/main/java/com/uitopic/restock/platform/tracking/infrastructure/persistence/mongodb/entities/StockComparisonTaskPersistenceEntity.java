package com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.entities;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.DeviceId;
import com.uitopic.restock.platform.shared.infrastructure.persistence.mongodb.entities.AuditableAbstractPersistenceEntity;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.ComparisonResult;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.StockRecord;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.TaskStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Persistence entity representing a stock comparison task, which is stored in the MongoDB collection "stock_comparison_tasks". This entity extends the AuditableAbstractPersistenceEntity to include common auditing fields such as createdAt and updatedAt. It contains fields for the physical stock, system stock, task status, comparison result, and device ID associated with the task.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Document(collection = "stock_comparison_tasks")
public class StockComparisonTaskPersistenceEntity extends AuditableAbstractPersistenceEntity {

    /**
     * The stock record obtained from the physical count, which is used for comparison against the system stock. This field is set when the task is created and remains unchanged throughout the task's lifecycle.
     */
    private StockRecord physicalStock;

    /**
     * The stock record obtained from the system, which is used for comparison against the physical stock. This field is set when the task is created and remains unchanged throughout the task's lifecycle.
     */
    private StockRecord systemStock;

    /**
     * The current status of the task, used for tracking progress and identifying tasks that are in progress, completed, cancelled, or failed.
     */
    private TaskStatus status;

    /**
     * The result of the stock comparison, which can be either MATCH or MISMATCH. This field is set when the task is completed.
     */
    private ComparisonResult result;

    /**
     * The unique identifier of the device that performed the stock count, used for tracking inventory discrepancies.
     */
    private DeviceId deviceId;
}
