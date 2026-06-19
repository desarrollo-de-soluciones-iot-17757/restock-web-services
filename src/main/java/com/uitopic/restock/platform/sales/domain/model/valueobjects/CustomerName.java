package com.uitopic.restock.platform.sales.domain.model.valueobjects;

public record CustomerName(String name) {
    public CustomerName{
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Customer name cannot be null or blank");
        }
    }
}
