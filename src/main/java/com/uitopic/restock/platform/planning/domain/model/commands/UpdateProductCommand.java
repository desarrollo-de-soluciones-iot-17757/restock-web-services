package com.uitopic.restock.platform.planning.domain.model.commands;

import java.math.BigDecimal;

/**
 * Command to update the mutable fields of an existing
 * {@link com.uitopic.restock.platform.planning.domain.model.aggregates.Product}.
 *
 * <p>All fields are optional — a {@code null} value means "leave unchanged".
 * The aggregate root's {@code update()} method enforces this convention.</p>
 *
 * @param productId    the ID of the product to update
 * @param name         new product name, or {@code null} to keep current
 * @param description  new description, or {@code null} to keep current
 * @param sku          new SKU, or {@code null} to keep current
 * @param imageUrl     new image URL, or {@code null} to keep current
 * @param sellingPrice new selling price, or {@code null} to keep current
 */
public record UpdateProductCommand(
        String productId,
        String name,
        String description,
        String sku,
        String imageUrl,
        BigDecimal sellingPrice
) {}
