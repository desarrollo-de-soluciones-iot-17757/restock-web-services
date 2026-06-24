package com.uitopic.restock.platform.subscriptions.domain.model.valueobjects;

public record BusinessId(String businessId) {

    public BusinessId {
        if (businessId == null || businessId.isBlank()) {
            throw new IllegalArgumentException("Business ID cannot be blank or empty");
        }
    }
}
