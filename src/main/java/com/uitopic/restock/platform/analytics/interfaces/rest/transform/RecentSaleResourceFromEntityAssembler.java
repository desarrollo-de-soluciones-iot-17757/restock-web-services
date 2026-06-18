package com.uitopic.restock.platform.analytics.interfaces.rest.transform;

import com.uitopic.restock.platform.analytics.application.internal.outboundservices.acl.ExternalSalesService.RecentSaleItem;
import com.uitopic.restock.platform.analytics.interfaces.rest.resources.RecentSaleResource;

/** Utility class responsible for converting between RecentSaleItem and RecentSaleResource. */
public class RecentSaleResourceFromEntityAssembler {

    private RecentSaleResourceFromEntityAssembler() {
        throw new IllegalStateException("Utility class");
    }

    /** Converts a RecentSaleItem to a RecentSaleResource. */
    public static RecentSaleResource toResourceFromEntity(RecentSaleItem item) {
        if (item == null) return null;

        return new RecentSaleResource(
                item.saleId(),
                item.branchId(),
                item.totalAmount(),
                item.saleDate(),
                item.status()
        );
    }
}
