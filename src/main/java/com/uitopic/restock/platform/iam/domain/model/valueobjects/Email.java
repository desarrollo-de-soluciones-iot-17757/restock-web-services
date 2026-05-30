package com.uitopic.restock.platform.iam.domain.model.valueobjects;

public record Email(String email) {

    public Email {
        if (email == null || !email.matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$"))
            throw new IllegalArgumentException("Invalid email format: " + email);
    }
}
