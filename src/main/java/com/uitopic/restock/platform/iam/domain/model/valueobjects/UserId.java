package com.uitopic.restock.platform.iam.domain.model.valueobjects;

public record UserId(String userId) {

    public UserId {
        if (userId == null || userId.isBlank())
            throw new IllegalArgumentException("User ID cannot be null or blank");
    }
}
