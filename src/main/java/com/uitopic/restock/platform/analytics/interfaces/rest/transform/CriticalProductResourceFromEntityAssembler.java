package com.uitopic.restock.platform.analytics.interfaces.rest.transform;

import com.uitopic.restock.platform.analytics.application.internal.outboundservices.acl.AnalyticsExternalResourcesService.CriticalProductItem;
import com.uitopic.restock.platform.analytics.interfaces.rest.resources.CriticalProductResource;

/** Utility class responsible for converting between CriticalProductItem and CriticalProductResource. */
public class CriticalProductResourceFromEntityAssembler {

    private CriticalProductResourceFromEntityAssembler() {
        throw new IllegalStateException("Utility class");
    }

    /** Converts a CriticalProductItem to a CriticalProductResource. */
    public static CriticalProductResource toResourceFromEntity(CriticalProductItem item) {
        if (item == null) return null;

        return new CriticalProductResource(
                item.productId(),
                item.productName(),
                item.supplyId(),
                item.totalStock(),
                item.minStock(),
                item.maxStock(),
                item.stockDeficit(),
                item.branchName(),
                item.branchId(),
                item.unitMeasurement()
        );
    }
}
