package com.uitopic.restock.platform.tracking.domain.model.aggregates;

import com.uitopic.restock.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.DeviceId;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.TaskStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Abstract base class representing a generic task in the inventory tracking system. This class provides common properties and methods for all types of tasks, such as stock comparison tasks and inventory update tasks.
 */
@Getter
@Setter
@NoArgsConstructor
public abstract class Task extends AbstractDomainAggregateRoot<Task> {

    /**
     * The unique identifier of the task, used for tracking and referencing the task in the system.
     */
    private String id;

    /**
     * The current status of the task, used for tracking progress and identifying tasks that are in progress, completed, cancelled, or failed.
     */
    private TaskStatus status = TaskStatus.IN_PROGRESS;

    /**
     * The timestamp when the task was created, used for tracking the age of the task and identifying stale tasks.
     */
    private Instant createdAt;

    /**
     * The device ID associated with the task, used for tracking inventory discrepancies.
     */
    private DeviceId deviceId;

    public Task(DeviceId deviceId) {
        this.createdAt = Instant.now();
        this.deviceId = deviceId;
    }

    /**
     * Creates a new task with the specified device ID and creation timestamp.
     */
    public void complete() {
        this.status = TaskStatus.COMPLETED;
    }

    /**
     * Marks the task as canceled, indicating that it was intentionally stopped before completion.
     */
    public void cancel() {
        this.status = TaskStatus.CANCELLED;
    }

    /**
     * Marks the task as failed, indicating that an error occurred during processing.
     */
    public void fail() {
        this.status = TaskStatus.FAILED;
    }
}
