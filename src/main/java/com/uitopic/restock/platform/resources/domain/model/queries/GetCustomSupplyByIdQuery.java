package com.uitopic.restock.platform.resources.domain.model.queries;

/**
 * Query to get a custom supply by its identifier.
 *
 * @param customSupplyId custom supply identifier
 */
public record GetCustomSupplyByIdQuery(
        String customSupplyId
) {
    public GetCustomSupplyByIdQuery {
        if (customSupplyId == null || customSupplyId.isBlank()) {
            throw new IllegalArgumentException("Custom supply ID cannot be null or blank");
        }
    }
}