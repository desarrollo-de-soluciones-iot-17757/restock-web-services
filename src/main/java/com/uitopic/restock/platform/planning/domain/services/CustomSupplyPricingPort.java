package com.uitopic.restock.platform.planning.domain.services;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Domain service <em>port</em> (output port / secondary port) used by the {@code planning}
 * bounded context to obtain pricing data from the {@code resources} bounded context without
 * introducing a direct compile-time coupling to its internals.
 *
 * <p>This interface belongs to the <strong>domain layer</strong> of {@code planning}.
 * Its implementation lives in the <strong>infrastructure layer</strong>
 * ({@link com.uitopic.restock.platform.planning.infrastructure.acl.CustomSupplyPricingAdapter}),
 * which queries the {@code resources} BC's own query service.  This is the classic
 * <em>Anti-Corruption Layer</em> (ACL) / <em>Ports &amp; Adapters</em> pattern.</p>
 *
 * <h3>Usage</h3>
 * The {@code planning} application service calls {@link #getUnitPrice(String)} before
 * constructing an {@link com.uitopic.restock.platform.planning.domain.model.entities.Ingredient},
 * so that {@code totalCost = quantity × unitPrice} can be computed correctly.</p>
 */
public interface CustomSupplyPricingPort {

    /**
     * Returns the current unit price of the custom supply identified by {@code customSupplyId}.
     *
     * @param customSupplyId the ID of the custom supply in the {@code resources} BC
     * @return an {@link Optional} containing the {@link BigDecimal} unit price,
     *         or {@link Optional#empty()} if the supply does not exist or has no price set
     */
    Optional<BigDecimal> getUnitPrice(String customSupplyId);
}
