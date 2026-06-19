package com.uitopic.restock.platform.tracking.domain.model.valueobjects;

/**
    * This enum represents the status of a conciliation task. It can be either pending, resolved manually, or resolved automatically.
 */
public enum ConciliationTaskStatus {
    PENDING,
    RESOLVED_MANUALLY,
    RESOLVED_AUTOMATICALLY
}