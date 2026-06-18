package com.uitopic.restock.platform.sales.application.internal.queryservices;

import com.uitopic.restock.platform.sales.domain.model.aggregates.SalesOrder;
import com.uitopic.restock.platform.sales.domain.model.queries.GetSalesOrderByIdQuery;
import com.uitopic.restock.platform.sales.domain.model.queries.GetSalesOrdersByAccountIdQuery;
import com.uitopic.restock.platform.sales.domain.model.queries.GetSalesOrdersByBranchIdQuery;
import com.uitopic.restock.platform.sales.domain.repositories.SalesOrderRepository;
import com.uitopic.restock.platform.sales.domain.services.SalesOrderQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Application service for SalesOrder read operations.
 * Implements the Query Service contract using CQRS pattern handlers.
 */
@Slf4j
@Service
public class SalesOrderQueryServiceImpl implements SalesOrderQueryService {

    private final SalesOrderRepository salesOrderRepository;

    /**
     * Constructor with dependency injection of the sales order repository.
     *
     * @param salesOrderRepository repository for accessing sales order data
     */
    public SalesOrderQueryServiceImpl(SalesOrderRepository salesOrderRepository) {
        this.salesOrderRepository = salesOrderRepository;
    }

    /**
     * Retrieves a sales order by its identifier.
     *
     * @param query query with the sales order identifier (orderId)
     * @return an Optional containing the sales order if found
     */
    @Override
    public Optional<SalesOrder> handle(GetSalesOrderByIdQuery query) {
        log.debug("Querying sales order by id='{}'", query.orderId());
        return salesOrderRepository.findById(query.orderId());
    }

    /**
     * Retrieves all sales orders from an account.
     *
     * @param query query with the account identifier (accountId)
     * @return list of sales orders for the account
     */
    @Override
    public List<SalesOrder> handle(GetSalesOrdersByAccountIdQuery query) {
        log.debug("Querying sales orders by accountId='{}'", query.accountId());
        var results = salesOrderRepository.findByAccountId(query.accountId());

        log.debug("Found {} sales orders for accountId='{}'", results.size(), query.accountId());
        return results;
    }

    /**
     * Retrieves all sales orders from a specific branch.
     *
     * @param query query with the branch identifier (branchId)
     * @return list of sales orders for the branch
     */
    @Override
    public List<SalesOrder> handle(GetSalesOrdersByBranchIdQuery query) {
        log.debug("Querying sales orders by branchId='{}'", query.branchId());
        var results = salesOrderRepository.findByBranchId(query.branchId());
        log.debug("Found {} sales orders for branchId='{}'", results.size(), query.branchId());
        return results;
    }
}