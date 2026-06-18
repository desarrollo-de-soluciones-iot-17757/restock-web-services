package com.uitopic.restock.platform.sales.interfaces.rest.transform;

import com.uitopic.restock.platform.sales.domain.model.commands.CreateSalesOrderCommand;
import com.uitopic.restock.platform.sales.interfaces.rest.resources.CreateSalesOrderResource;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;

public class CreateSalesOrderCommandFromResourceAssembler {

    public static CreateSalesOrderCommand toCommandFromResource(String accountId, CreateSalesOrderResource resource) {
        return new CreateSalesOrderCommand(
                new AccountId(accountId),
                resource.branchId()
        );
    }
}