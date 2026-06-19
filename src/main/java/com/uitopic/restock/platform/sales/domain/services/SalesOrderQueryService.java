package com.uitopic.restock.platform.sales.domain.services;

import com.uitopic.restock.platform.sales.domain.model.aggregates.SalesOrder;
import com.uitopic.restock.platform.sales.domain.model.queries.GetSalesOrderByIdQuery;
import com.uitopic.restock.platform.sales.domain.model.queries.GetSalesOrdersByAccountIdQuery;
import com.uitopic.restock.platform.sales.domain.model.queries.GetSalesOrdersByBranchIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Query service contract for SalesOrder read operations.
 */
public interface SalesOrderQueryService {

    /**
     * Retrieves a sales order by its identifier.
     *
     * @param query query with the sales order identifier
     * @return sales order if found
     */
    Optional<SalesOrder> handle(GetSalesOrderByIdQuery query);

    /**
     * Retrieves all sales orders from an account.
     *
     * @param query query with the account identifier
     * @return list of sales orders for the account
     */
    List<SalesOrder> handle(GetSalesOrdersByAccountIdQuery query);

    /**
     * Retrieves all sales orders from a specific branch.
     *
     * @param query query with the branch identifier
     * @return list of sales orders for the branch
     */
    List<SalesOrder> handle(GetSalesOrdersByBranchIdQuery query);
}