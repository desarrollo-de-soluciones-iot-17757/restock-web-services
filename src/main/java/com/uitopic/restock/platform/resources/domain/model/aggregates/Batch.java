package com.uitopic.restock.platform.resources.domain.model.aggregates;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.Stock;
import com.uitopic.restock.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import lombok.*;

import java.time.LocalDate;

/**
 * Aggregate root representing a batch of stock in a branch.
 *
 * A batch stores the current quantity available for a custom supply in a
 * specific branch. It keeps references to the custom supply, branch and account
 * that own the stock.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Batch extends AbstractDomainAggregateRoot<Batch> {

    /**
     * Unique identifier of the batch.
     */
    private String id;

    /**
     * Business code used to identify the batch.
     */
    private String code;

    /**
     * Current stock available in this batch.
     */
    private Stock currentStock;

    /**
     * Identifier of the custom supply associated with this batch.
     */
    private String customSupplyId;

    /**
     * Identifier of the branch where this batch is stored.
     */
    private String branchId;

    /**
     * Account that owns this batch.
     */
    private AccountId accountId;

    /**
     * Optional expiration date of the batch.
     */
    private LocalDate expirationDate;

    /**
     * Date when the batch entered the inventory.
     */
    private LocalDate entryDate;

    /**
     * Creates a new batch.
     *
     * @param code batch code
     * @param currentStock current stock
     * @param customSupplyId custom supply identifier
     * @param branchId branch identifier
     * @param accountId account identifier
     * @param expirationDate expiration date
     * @param entryDate inventory entry date
     */
    @Builder
    public Batch(
            String code,
            Stock currentStock,
            String customSupplyId,
            String branchId,
            String accountId,
            LocalDate expirationDate,
            LocalDate entryDate
    ) {
        validateText(code, "Batch code");
        validateText(customSupplyId, "Custom supply ID");
        validateText(branchId, "Branch ID");
        validateText(accountId, "Account ID");

        if (currentStock == null) {
            throw new IllegalArgumentException("Current stock cannot be null");
        }

        this.code = code;
        this.currentStock = currentStock;
        this.customSupplyId = customSupplyId;
        this.branchId = branchId;
        this.accountId = new AccountId(accountId);
        this.expirationDate = expirationDate;
        this.entryDate = entryDate;
    }

    /**
     * Increases the current stock of this batch.
     *
     * @param quantity quantity to add
     */
    public void increase(Stock quantity) {
        this.currentStock = this.currentStock.add(quantity);
    }

    /**
     * Subtracts from the current stock of this batch.
     *
     * @param quantity quantity to subtract
     */
    public void subtract(Stock quantity) {
        this.currentStock = this.currentStock.subtract(quantity);
    }

    /**
     * Transfers stock from this batch to another batch.
     *
     * @param targetBatch target batch
     * @param quantity quantity to transfer
     */
    public void transferTo(Batch targetBatch, Stock quantity) {
        if (targetBatch == null) {
            throw new IllegalArgumentException("Target batch cannot be null");
        }

        if (!this.customSupplyId.equals(targetBatch.customSupplyId)) {
            throw new IllegalArgumentException("Stock can only be transferred between batches of the same custom supply");
        }

        this.subtract(quantity);
        targetBatch.increase(quantity);
    }

    private void validateStockOperation(Stock quantity) {
        if (quantity == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }

        if (this.currentStock == null) {
            throw new IllegalStateException("Current stock is not initialized");
        }

        if (!this.currentStock.unitMeasurement().equals(quantity.unitMeasurement())) {
            throw new IllegalArgumentException("Stock unit measurement does not match");
        }

        if (quantity.stock() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
    }

    private void validateText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
        }
    }

    /**
     * Changes the batch code.
     *
     * @param code new batch code
     */
    public void changeCode(String code) {
        validateText(code, "Batch code");
        this.code = code;
    }

    /**
     * Changes the current stock.
     *
     * @param currentStock new current stock
     */
    public void changeCurrentStock(Stock currentStock) {
        if (currentStock == null) {
            throw new IllegalArgumentException("Current stock cannot be null");
        }

        this.currentStock = currentStock;
    }

    /**
     * Changes the expiration date.
     *
     * @param expirationDate new expiration date
     */
    public void changeExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }
}