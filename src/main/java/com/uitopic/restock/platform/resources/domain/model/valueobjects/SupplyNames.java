package com.uitopic.restock.platform.resources.domain.model.valueobjects;

/**
 * Enumeration of predefined supply category names used in the resources bounded
 * context.
 *
 * <p>
 * Used as the {@code category} field in
 * {@link com.uitopic.restock.platform.resources.domain.model.entities.Supply}
 * to classify supply items by their type. This enum provides type-safe category
 * values
 * and avoids free-text category strings in the domain model.
 */
public enum SupplyNames {
    /** Potato supply category. */
    POTATO,
    /** Onion supply category. */
    ONION,
    /** Carrot supply category. */
    CARROT,
    /** Lettuce supply category. */
    LETTUCE,
    /** Tomato supply category. */
    TOMATO
}
