package com.uitopic.restock.platform.analytics.domain.model.valueobjects;

/**
 * Abstract base class for incrementable metric values.
 * Provides common increment/decrement operations with null-safety
 * and bounds checking.
 */
public abstract class Incrementable {

    /** Current numeric value of this incrementable. */
    protected Long value;

    /** Identifier of the resource this incrementable tracks. */
    protected String resourceId;

    /**
     * Creates an Incrementable with an initial value and resource identifier.
     *
     * @param value      initial value (must not be null and must be non-negative)
     * @param resourceId resource identifier (must not be null or blank)
     */
    public Incrementable(Long value, String resourceId) {
        if (value == null) {
            throw new IllegalArgumentException("value cannot be null");
        }
        if (value < 0) {
            throw new IllegalArgumentException("value cannot be negative");
        }
        if (resourceId == null || resourceId.isBlank()) {
            throw new IllegalArgumentException("resourceId cannot be null or blank");
        }
        this.value = value;
        this.resourceId = resourceId;
    }

    /** Increments the value by one. */
    public void increment() {
        this.value++;
    }

    /**
     * Increments the value by a given amount.
     *
     * @param value amount to increment by (must be non-negative)
     */
    public void incrementBy(Long value) {
        if (value == null || value < 0) {
            throw new IllegalArgumentException("increment value must be non-negative");
        }
        this.value += value;
    }

    /**
     * Decrements the value by one, ensuring it does not go below zero.
     */
    public void decrement() {
        if (this.value <= 0) {
            throw new IllegalStateException("cannot decrement below zero");
        }
        this.value--;
    }

    /**
     * Decrements the value by a given amount, ensuring it does not go below zero.
     *
     * @param value amount to decrement by (must be non-negative)
     */
    public void decrementBy(Long value) {
        if (value == null || value < 0) {
            throw new IllegalArgumentException("decrement value must be non-negative");
        }
        if (this.value - value < 0) {
            throw new IllegalStateException("cannot decrement below zero");
        }
        this.value -= value;
    }

    /**
     * Returns the current value.
     *
     * @return current value
     */
    public Long getValue() {
        return value;
    }

    /**
     * Returns the resource identifier.
     *
     * @return resource identifier
     */
    public String getResourceId() {
        return resourceId;
    }
}
