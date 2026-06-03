package com.uitopic.restock.platform.planning.domain.model.enums;

/**
 * Enumeration representing the type of a {@link com.uitopic.restock.platform.planning.domain.model.aggregates.Product}.
 * <ul>
 *   <li>{@link #RECIPE} – a product assembled from a list of ingredients (e.g., a prepared dish).</li>
 *   <li>{@link #KIT}    – a product bundled from a list of components (e.g., a gift set).</li>
 * </ul>
 */
public enum ProductType {
    RECIPE,
    KIT
}
