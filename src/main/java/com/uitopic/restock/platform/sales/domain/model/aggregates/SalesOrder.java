package com.uitopic.restock.platform.sales.domain.model.aggregates;

import com.uitopic.restock.platform.sales.domain.exceptions.SalesDomainException;
import com.uitopic.restock.platform.sales.domain.model.entities.SalesOrderItem;
import com.uitopic.restock.platform.sales.domain.model.events.SalesOrderCompletedEvent;
import com.uitopic.restock.platform.sales.domain.model.valueobjects.*;
import com.uitopic.restock.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.Money;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Aggregate root representing a sales order.
 * A sales order contains items (kits or recipes) and manages their lifecycle.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrder extends AbstractDomainAggregateRoot<SalesOrder> {

    private String id;
    private AccountId accountId;
    private String branchId;
    private SalesOrderStatus status;
    private LocalDateTime createdAt;
    private List<SalesOrderItem> items = new ArrayList<>();
    private SalesOrderPricing pricing;

    /**
     * Creates a new SalesOrder from a command.
     * Initializes with status PENDING.
     */
    public SalesOrder(AccountId accountId, String branchId) {
        this.accountId = accountId;
        this.branchId = branchId;
        this.status = SalesOrderStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.items = new ArrayList<>();
        calculateTotal();
    }

    /**
     * Adds an item to the order.
     * Validates that the order is PENDING and that the item type matches the order type.
     *
     * @param item the item to add
     */
    public void addItem(SalesOrderItem item) {
        validateIsPending();
        if (item == null) throw new IllegalArgumentException("Item cannot be null");
        this.items.add(item);
        calculateTotal();
    }

    /**
     * Removes an item from the order by its ID.
     * Validates that the order is PENDING.
     *
     * @param itemId the item ID to remove
     */
    public void removeItem(String itemId) {
        validateIsPending();

        if (itemId == null || itemId.isBlank()) {
            throw new IllegalArgumentException("Item ID cannot be null or blank");
        }

        this.items.removeIf(item -> item.getId().equals(itemId));
        calculateTotal();
    }

    /**
     * Assigns batch consumptions to an item in the order.
     *
     * @param itemId the item ID
     * @param batches the batch consumptions
     */
    public void assignBatchConsumptions(String itemId, List<BatchConsumption> batches) {
        validateIsPending();
        items.stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item not found: " + itemId))
                .assignBatches(batches);
    }

    /**
     * Completes the order (confirms the sale).
     * Validates that the order is PENDING, not empty, and all items have batches assigned.
     */
    public void complete() {
        validateIsPending();
        if (items.isEmpty()) throw new IllegalStateException("Cannot confirm an empty order");
        validateAllItemsHaveBatches();
        this.status = SalesOrderStatus.COMPLETED;
        registerDomainEvent(new SalesOrderCompletedEvent(
                this.id,
                this.accountId.getAccountId(),
                this.branchId,
                getAllBatchConsumptions()
        ));
    }

    /**
     * Cancels the order.
     * Only PENDING orders can be cancelled, but we allow cancelling COMPLETED as well
     * based on business requirements (could be restricted further).
     */
    public void cancel() {
        if (this.status == SalesOrderStatus.COMPLETED)
            throw new SalesDomainException("Cannot cancel a completed order");
        if (this.status == SalesOrderStatus.CANCELLED)
            throw new SalesDomainException("Order is already cancelled");
        this.status = SalesOrderStatus.CANCELLED;
    }

    /**
     * Calculates the total pricing for the order.
     * Applies 18% tax on the subtotal.
     */
    public void calculateTotal() {
        BigDecimal subtotalAmount = items.stream()
                .map(item -> item.calculateSubtotal().getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String currency = items.isEmpty() ? "USD" : items.get(0).getUnitPrice().getCurrencyCode();
        Money subtotal = new Money(subtotalAmount, currency);

        // Apply 18% tax
        BigDecimal taxAmount = subtotalAmount.multiply(new BigDecimal("0.18"));
        Money taxes = new Money(taxAmount, currency);

        BigDecimal totalAmount = subtotalAmount.add(taxAmount);
        Money total = new Money(totalAmount, currency);

        this.pricing = new SalesOrderPricing(subtotal, taxes, total);
    }

    /**
     * Gets all batch consumptions from all items (flattened).
     *
     * @return list of batch consumptions
     */
    public List<BatchConsumption> getAllBatchConsumptions() {
        return items.stream()
                .flatMap(item -> item.getConsumedBatches().stream())
                .collect(Collectors.toList());
    }

    // ==================== Private Validation Methods ====================

    private void validateIsPending() {
        if (this.status != SalesOrderStatus.PENDING) {
            throw new SalesDomainException(
                    "Order must be in PENDING status to perform this operation. Current status: " + this.status
            );
        }
    }

    private void validateAllItemsHaveBatches() {
        boolean missingBatches = items.stream()
                .anyMatch(i -> i.getConsumedBatches() == null || i.getConsumedBatches().isEmpty());
        if (missingBatches) {
            throw new IllegalStateException("All items must have batch consumptions resolved before completing the order");
        }
    }

    public List<SalesOrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

}
