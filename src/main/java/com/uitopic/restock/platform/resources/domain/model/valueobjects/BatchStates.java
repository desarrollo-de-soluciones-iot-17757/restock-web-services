package com.uitopic.restock.platform.resources.domain.model.valueobjects;

/**
 * Enum representing the possible states of a batch in the inventory management system.
 * - ACTIVE: The batch is currently active and available for use.
 * - DEPLETED: The batch has been fully used and is no longer available.
 * - EXPIRED: The batch has passed its expiration date and should not be used.
 */
public enum BatchStates {
    ACTIVE,
    DEPLETED,
    EXPIRED
}
