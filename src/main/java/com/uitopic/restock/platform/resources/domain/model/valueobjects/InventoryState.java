package com.uitopic.restock.platform.resources.domain.model.valueobjects;

/**
 * Enum representing the inventory state of a supply item at a given branch
 * within the resources bounded context.
 *
 * <p>Used by {@link com.uitopic.restock.platform.resources.domain.model.entities.Inventory}
 * to indicate whether a supply is fully stocked, running low, or completely depleted.
 * Stored as a plain string in MongoDB via the registered
 * {@link com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.converters.InventoryStateWriteConverter}
 * and {@link com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.converters.InventoryStateReadConverter}.
 */
public enum InventoryState {
    /** The supply has no stock remaining. */
    OUTOFSTOCK,
    /** The supply has stock above the minimum threshold. */
    WITHSTOCK,
    /** The supply has stock but it is at or below the minimum threshold. */
    LOWSTOCK
}
