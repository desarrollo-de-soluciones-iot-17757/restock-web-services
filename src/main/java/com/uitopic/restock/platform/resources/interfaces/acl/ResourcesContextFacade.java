package com.uitopic.restock.platform.resources.interfaces.acl;

/**
 * Inbound ACL facade — exposes Resources BC inventory operations to other bounded contexts.
 */
public interface ResourcesContextFacade {
    double subtractSupplyStock(String branchId, String supplyId, Integer quantity);
    void addSupplyStockBack(String branchId, String supplyId, Integer quantity, String unit);
    void adjustStock(String branchId, String supplyId, Integer adjustedQuantity, String unit);
}
