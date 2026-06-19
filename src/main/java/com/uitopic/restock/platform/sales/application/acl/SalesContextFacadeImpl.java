package com.uitopic.restock.platform.sales.application.acl;

import com.uitopic.restock.platform.sales.domain.model.queries.GetSalesOrderByIdQuery;
import com.uitopic.restock.platform.sales.domain.services.SalesOrderQueryService;
import com.uitopic.restock.platform.sales.interfaces.acl.SalesContextFacade;
import org.springframework.stereotype.Service;

@Service
public class SalesContextFacadeImpl implements SalesContextFacade {

    private final SalesOrderQueryService salesOrderQueryService;

    public SalesContextFacadeImpl(SalesOrderQueryService salesOrderQueryService) {
        this.salesOrderQueryService = salesOrderQueryService;
    }

    @Override
    public boolean isSalesOrderValid(String salesOrderId) {
        var query = new GetSalesOrderByIdQuery(salesOrderId);
        return salesOrderQueryService.handle(query).isPresent();
    }

    @Override
    public double getSalesOrderTotal(String salesOrderId) {
        var query = new GetSalesOrderByIdQuery(salesOrderId);
        return salesOrderQueryService.handle(query)
                .map(order -> order.getPricing().total().amount().doubleValue())
                .orElse(0.0);
    }

    @Override
    public String getSalesOrderStatus(String salesOrderId) {
        var query = new GetSalesOrderByIdQuery(salesOrderId);
        return salesOrderQueryService.handle(query)
                .map(order -> order.getStatus().name())
                .orElse("");
    }
}