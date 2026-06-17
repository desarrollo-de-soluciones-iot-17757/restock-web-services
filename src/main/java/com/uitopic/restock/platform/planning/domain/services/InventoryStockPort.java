package com.uitopic.restock.platform.planning.domain.services;

/**
 * Domain service <em>port</em> (output port / secondary port) used by the {@code planning}
 * bounded context to obtain aggregated inventory stock data from the {@code resources} bounded
 * context without introducing a direct compile-time coupling to its internals.
 *
 * <p>This interface belongs to the <strong>domain layer</strong> of {@code planning}.
 * Its implementation lives in the <strong>infrastructure layer</strong>
 * ({@link com.uitopic.restock.platform.planning.infrastructure.acl.InventoryStockAdapter}),
 * which delegates to the {@code resources} BC's inbound ACL facade. This is the classic
 * <em>Anti-Corruption Layer</em> (ACL) / <em>Ports &amp; Adapters</em> pattern.</p>
 */
public interface InventoryStockPort {

    /**
     * Returns the total aggregated stock for a given custom supply in a branch.
     *
     * @param customSupplyId the ID of the custom supply in the {@code resources} BC
     * @param branchId       the branch where stock is stored
     * @return total available stock (sum of all batch quantities), or 0.0 if none
     */
    double getTotalStock(String customSupplyId, String branchId);
}
