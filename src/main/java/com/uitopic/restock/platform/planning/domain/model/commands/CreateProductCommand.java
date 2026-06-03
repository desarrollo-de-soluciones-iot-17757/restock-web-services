package com.uitopic.restock.platform.planning.domain.model.commands;

import java.math.BigDecimal;

/**
 * Command to create a new {@link com.uitopic.restock.platform.planning.domain.model.aggregates.Product}
 * within the {@code planning} bounded context.
 *
 * @param accountId    tenant account owning the product
 * @param name         human-readable product name, unique per account
 * @param description  optional description of the product
 * @param sku          stock-keeping unit identifier, unique per account
 * @param type         product type: {@code "RECIPE"} or {@code "KIT"}
 * @param imageUrl     optional URL for the product image
 * @param sellingPrice selling price of the finished product
 */
public record CreateProductCommand(
        String accountId,
        String name,
        String description,
        String sku,
        String type,
        String imageUrl,
        BigDecimal sellingPrice
) {}
