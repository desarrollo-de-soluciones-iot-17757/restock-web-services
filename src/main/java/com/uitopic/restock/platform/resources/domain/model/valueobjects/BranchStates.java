package com.uitopic.restock.platform.resources.domain.model.valueobjects;

/**
 * Enumeration representing the possible operational states of a
 * {@link com.uitopic.restock.platform.resources.domain.model.aggregates.Branch}
 * within the resources bounded context.
 *
 * <p>Used to control whether a branch is available for operations. Branches are created
 * in the {@code ACTIVE} state and transition to {@code INACTIVE} when logically deleted
 * via {@link com.uitopic.restock.platform.resources.domain.model.aggregates.Branch#deactivate()}.
 * This supports soft-delete semantics without physically removing branch records.
 */
public enum BranchStates {
    /** The branch is operational and available for use. */
    ACTIVE,
    /** The branch has been logically deleted and is no longer operational. */
    INACTIVE
}
