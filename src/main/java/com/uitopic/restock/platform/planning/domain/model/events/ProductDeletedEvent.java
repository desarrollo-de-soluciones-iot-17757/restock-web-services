package com.uitopic.restock.platform.planning.domain.model.events;

/**
 * Domain event published when a
 * {@link com.uitopic.restock.platform.planning.domain.model.aggregates.Product}
 * is deleted within the {@code planning} bounded context.
 *
 * <p>Consumers (e.g., other bounded contexts that cache product data) can listen
 * for this event via Spring's {@link org.springframework.context.event.EventListener}
 * to perform compensating actions such as cache invalidation.</p>
 *
 * @param productId the unique identifier of the deleted product
 * @param accountId the tenant account that owned the product
 */
public record ProductDeletedEvent(String productId, String accountId) {}
