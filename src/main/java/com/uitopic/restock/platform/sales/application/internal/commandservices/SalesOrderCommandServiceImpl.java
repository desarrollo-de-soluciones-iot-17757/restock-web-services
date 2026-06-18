package com.uitopic.restock.platform.sales.application.internal.commandservices;
import com.uitopic.restock.platform.sales.domain.model.aggregates.SalesOrder;
import com.uitopic.restock.platform.sales.domain.model.commands.*;
import com.uitopic.restock.platform.sales.domain.repositories.SalesOrderRepository;
import com.uitopic.restock.platform.sales.domain.services.SalesOrderCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Application service implementation for SalesOrder write operations.
 * Orchestrates domain aggregate behaviors and persistence.
 */
@Slf4j
@Service
public class SalesOrderCommandServiceImpl implements SalesOrderCommandService {

    private final SalesOrderRepository salesOrderRepository;

    public SalesOrderCommandServiceImpl(
            SalesOrderRepository salesOrderRepository) {
        this.salesOrderRepository = salesOrderRepository;
    }

    @Override
    public SalesOrder handle(CreateSalesOrderCommand command) {
        SalesOrder salesOrder = new SalesOrder(
                command.accountId(),
                command.branchId()
        );
        return salesOrderRepository.save(salesOrder);
    }

}