package com.uitopic.restock.platform.sales.domain.model.entities;
import com.uitopic.restock.platform.sales.domain.model.valueobjects.BatchConsumption;
import com.uitopic.restock.platform.sales.domain.model.valueobjects.ProductType;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.Money;

import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@With
@EqualsAndHashCode
public class SalesOrderItem {

    /**
     * Unique identifier for this item (UUID).
     */
    private String id;

    /**
     * Product identifier (kitId or recipeId).
     */
    private String productId;

    /**
     * Type of the product (e.g., KIT, RECIPE).
     */
    private ProductType productType;

    /**
     * Snapshot of the product name at the time of sale.
     */
    private String nameSnapshot;

    /**
     * Quantity of the product ordered.
     */
    private int quantity;

    /**
     * Unit price of the product at the time of sale.
     */
    private Money unitPrice;

    /**
     * List of batches consumed to fulfill this sales order item.
     */
    private List<BatchConsumption> consumedBatches = new ArrayList<>();

    /**
     * Calculates the subtotal for this item.
     *
     * @return subtotal (unitPrice * quantity) as a Money object
     */
    public Money calculateSubtotal() {
        BigDecimal subtotalAmount = unitPrice.getAmount().multiply(BigDecimal.valueOf(quantity));
        return new Money(subtotalAmount, unitPrice.getCurrencyCode());
    }

    /**
     * Adds a single batch consumption record to this item.
     *
     * @param batchConsumption the batch consumption details
     */
    public void addConsumedBatch(BatchConsumption batchConsumption) {
        if (batchConsumption == null) {
            throw new IllegalArgumentException("Batch consumption cannot be null");
        }
        this.consumedBatches.add(batchConsumption);
    }

    /**
     * Assigns a complete list of batch consumptions to this item.
     *
     * @param batches list of batch consumptions to fulfill the item quantity
     */
    public void assignBatches(List<BatchConsumption> batches) {
        if (batches == null || batches.isEmpty()) {
            throw new IllegalArgumentException("Batches cannot be empty");
        }
        this.consumedBatches = new ArrayList<>(batches);
    }

    private void validateProductData(String productId, BigDecimal unitPrice, int quantity) {
        if (productId == null || productId.isBlank()) {
            throw new IllegalArgumentException("Product ID cannot be null or blank");
        }
        if (unitPrice == null) {
            throw new IllegalArgumentException("Unit price cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
    }
}
