package com.uitopic.restock.platform.sales.interfaces.acl;

/**
 * Inbound ACL facade — exposes Sales bounded context operations
 * to other bounded contexts (e.g., Reports, Inventory, Billing).
 */
public interface SalesContextFacade {

    /**
     * Verifies if a specific sales order exists and is valid for processing.
     */
    boolean isSalesOrderValid(String salesOrderId);

    /**
     * Retrieves the total amount of a sales order for billing purposes.
     */
    double getSalesOrderTotal(String salesOrderId);

    /**
     * Retrieves the current operational status of a sales order as a standard text string.
     * <p>
     * Architecture Note: Returns a plain {@link String} instead of the internal SalesOrderStatus enum.
     * This prevents coupling external contexts to the internal state lifecycle of the Sales domain.
     * </p>
     *
     * @param salesOrderId the unique identifier of the sales order
     * @return the status name (e.g., "PENDING", "COMPLETED", "CANCELLED"), or an empty string if not found
     */
    String getSalesOrderStatus(String salesOrderId);

}