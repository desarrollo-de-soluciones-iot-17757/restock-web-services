package com.uitopic.restock.platform.resources.domain.model.valueobjects;

/**
 * Represents the content of a supply item, such as weight or volume.
 * This value object encapsulates the content value and ensures it is valid.
 *
 * @param content the content value, which must be a positive number and not exceed 10,000
 */
public record SupplyContent(
        Double content
) {

    // Validation logic to ensure content is a positive number and does not exceed 10,000
    public SupplyContent {
        if (content == null || content <= 0) {
            throw new IllegalArgumentException("Content must be a positive number.");
        }

        if (content > 10000) {
            throw new IllegalArgumentException("Content must not exceed 10,000.");
        }
    }

    /**
     * Returns the content value.
     *
     * @return the content value
     */
    public Double getContent() {
        return content;
    }
}
