package com.uitopic.restock.platform.resources.domain.model.valueobjects;

/**
 * Value object representing the content quantity of a single unit of a
 * {@link com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply}.
 *
 * <p>Captures the numeric amount of a supply unit (e.g., 500 for 500 ml, 1 for 1 kg).
 * The unit of measurement is tracked separately via
 * {@link com.uitopic.restock.platform.shared.domain.model.valueobjects.UnitMeasurement}.
 * This value object enforces that the content is always a positive number.
 *
 * @param content the content value, must be a positive number
 */
public record SupplyContent(
        Double content
) {

    /**
     * Compact constructor that validates the content value.
     *
     * @throws IllegalArgumentException if {@code content} is null or not positive
     */
    public SupplyContent {
        if (content == null || content <= 0) {
            throw new IllegalArgumentException("Content must be a positive number.");
        }
    }

    /**
     * Returns the content value.
     *
     * @return the content as a {@link Double}
     */
    public Double getContent() {
        return content;
    }
}
