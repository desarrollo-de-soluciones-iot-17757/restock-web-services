package com.uitopic.restock.platform.analytics.domain.model.valueobjects;

import java.time.LocalDate;

/**
 * Value object representing a date range with start and end dates.
 * Validates that start date is not null, end date is not null,
 * and end date is not before start date.
 */
public record DateRange(LocalDate startDate, LocalDate endDate) {

    /**
     * Compact constructor that validates the date range.
     */
    public DateRange {
        if (startDate == null) {
            throw new IllegalArgumentException("startDate cannot be null");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("endDate cannot be null");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("endDate must not be before startDate");
        }
    }

    /**
     * Returns the start date of the range.
     *
     * @return start date
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Returns the end date of the range.
     *
     * @return end date
     */
    public LocalDate getEndDate() {
        return endDate;
    }
}
