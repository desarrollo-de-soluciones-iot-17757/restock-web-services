package com.uitopic.restock.platform.sales.infrastructure.persistence.mongodb.entities;

import com.uitopic.restock.platform.sales.domain.model.entities.SalesOrderItem;
import com.uitopic.restock.platform.sales.domain.model.valueobjects.SalesOrderPricing;
import com.uitopic.restock.platform.sales.domain.model.valueobjects.SalesOrderStatus; // O tu enum/clase de estado
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.infrastructure.persistence.mongodb.entities.AuditableAbstractPersistenceEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Persistence entity representing a sales order in the MongoDB database.
 * This entity stores the aggregate root data including account ownership, branch assignment,
 * registered item snapshots, pricing information, and the order lifetime states.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Document(collection = "sales_orders")
public class SalesOrderPersistenceEntity extends AuditableAbstractPersistenceEntity {

    /**
     * Account that owns this sales order.
     */
    private AccountId accountId;

    /**
     * Identifier of the branch where this sales order was processed.
     */
    private String branchId;

    /**
     * Identifier of the user who registered this sales order.
     */
    private String registeredByUserId;

    /**
     * Name of the customer associated with the sales order.
     */
    private String customerName;

    /**
     * Type of the order (e.g., DINE_IN, TAKE_AWAY, DELIVERY).
     */
    private String orderType;

    /**
     * List of items registered within this sales order, including batch consumption snapshots.
     */
    private List<SalesOrderItem> items;

    /**
     * Detailed pricing details of the order, including subtotal, taxes, totals, and currency.
     */
    private SalesOrderPricing pricing;

    /**
     * Current operational status of the sales order.
     */
    private SalesOrderStatus status;

    /**
     * Timestamp indicating when the sales order was officially registered.
     */
    private LocalDateTime registeredAt;

    /**
     * Optional timestamp indicating when the sales order was cancelled.
     */
    private LocalDateTime cancelledAt;
}