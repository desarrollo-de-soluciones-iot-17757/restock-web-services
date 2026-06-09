package com.uitopic.restock.platform.resources.domain.model.queries;

/**
 * Query to get a base supply by its identifier.
 *
 * @param supplyId supply identifier
 */
public record GetSupplyByIdQuery(
        String supplyId
) {
    public GetSupplyByIdQuery {
        if (supplyId == null || supplyId.isBlank()) {
            throw new IllegalArgumentException("Supply ID cannot be null or blank");
        }
    }
}