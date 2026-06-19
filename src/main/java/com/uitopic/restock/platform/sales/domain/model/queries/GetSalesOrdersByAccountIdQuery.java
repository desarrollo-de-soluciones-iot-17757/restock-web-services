package com.uitopic.restock.platform.sales.domain.model.queries;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;

public record GetSalesOrdersByAccountIdQuery(
        AccountId accountId
) {

    public GetSalesOrdersByAccountIdQuery{
        if(accountId == null){
            throw new IllegalArgumentException("Account ID cannot be null or blank");
        }
    }

}
