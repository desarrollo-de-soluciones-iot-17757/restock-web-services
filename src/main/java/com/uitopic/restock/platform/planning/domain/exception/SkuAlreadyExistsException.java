package com.uitopic.restock.platform.planning.domain.exception;

/**
 * Exception thrown when attempting to create a
 * {@link com.uitopic.restock.platform.planning.domain.model.aggregates.Product}
 * with a SKU that already exists for the same account within the {@code planning} bounded context.
 */
public class SkuAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new {@code SkuAlreadyExistsException} with a descriptive message.
     *
     * @param sku       the duplicate SKU
     * @param accountId the account scope where the duplicate was found
     */
    public SkuAlreadyExistsException(String sku, String accountId) {
        super("A product with SKU '" + sku + "' already exists for account '" + accountId + "'.");
    }
}
