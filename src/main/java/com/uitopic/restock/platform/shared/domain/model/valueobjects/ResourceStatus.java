package com.uitopic.restock.platform.shared.domain.model.valueobjects;

/**
 * Enumeration representing the status of a resource in the system. The possible values are:
 * - ACTIVE: The resource is active and can be used or accessed.
 * - INACTIVE: The resource is inactive and cannot be used or accessed, but it has not been deleted. It may be temporarily unavailable or pending activation.
 * - DELETED: The resource has been deleted and is no longer available for use or access. It may be permanently removed from the system or marked as deleted for archival purposes.
 */
public enum ResourceStatus {
    ACTIVE,
    INACTIVE,
    DELETED
}
