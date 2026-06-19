package com.uitopic.restock.platform.sales.domain.repositories;

import com.uitopic.restock.platform.sales.domain.model.aggregates.SalesOrder;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;

import java.util.List;
import java.util.Optional;

/**
 * Domain repository port for SalesOrder aggregate persistence.
 * Pure DDD contract, completely agnostic of persistence frameworks and infrastructure.
 */
public interface SalesOrderRepository {

    /**
     * Saves a sales order.
     *
     * @param order the sales order to save
     * @return the saved sales order
     */
    SalesOrder save(SalesOrder order);

    /**
     * Finds a sales order by its identifier.
     *
     * @param id the sales order identifier
     * @return the sales order if found
     */
    Optional<SalesOrder> findById(String id);

    /**
     * Finds all sales orders.
     *
     * @return list of sales orders
     */
    List<SalesOrder> findAll();

    /**
     * Finds all sales orders that belong to an account.
     *
     * @param accountId account identifier
     * @return sales orders owned by the account
     */
    List<SalesOrder> findByAccountId(AccountId accountId);

    /**
     * Finds sales orders by branch identifier.
     *
     * @param branchId the branch identifier
     * @return list of sales orders for the branch
     */
    List<SalesOrder> findByBranchId(String branchId);

    /**
     * Deletes a sales order by its identifier.
     *
     * @param id the sales order identifier
     */
    void deleteById(String id);
}