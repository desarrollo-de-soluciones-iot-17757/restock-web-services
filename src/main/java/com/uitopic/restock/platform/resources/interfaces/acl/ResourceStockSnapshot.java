package com.uitopic.restock.platform.resources.interfaces.acl;

public record ResourceStockSnapshot(
        Double stock,
        String customSupplyId,
        String customSupplyName,
        String branchId,
        String accountId
) {
}
