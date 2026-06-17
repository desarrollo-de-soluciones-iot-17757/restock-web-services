package com.uitopic.restock.platform.shared.domain.model.valueobjects;

/**
 * Value object representing a User ID in the system.
 *
 * @param userId the unique identifier for a user, typically a UUID string
 */
public record UserId(
        String userId
) {

    public UserId {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("UserId cannot be null or blank");
        }
    }

    public String getValue() {
        return userId;
    }
}
