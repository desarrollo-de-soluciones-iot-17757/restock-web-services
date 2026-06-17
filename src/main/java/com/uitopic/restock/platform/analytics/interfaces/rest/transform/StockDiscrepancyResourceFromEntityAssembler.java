package com.uitopic.restock.platform.analytics.interfaces.rest.transform;

import com.uitopic.restock.platform.analytics.interfaces.rest.resources.StockDiscrepancyResource;

/** Utility class responsible for creating a conciliated StockDiscrepancyResource. */
public class StockDiscrepancyResourceFromEntityAssembler {

    private StockDiscrepancyResourceFromEntityAssembler() {
        throw new IllegalStateException("Utility class");
    }

    /** Creates a conciliated StockDiscrepancyResource indicating no discrepancy. */
    public static StockDiscrepancyResource conciliated() {
        return new StockDiscrepancyResource(
                null,
                0.0,
                0.0,
                0.0,
                null,
                null,
                true
        );
    }
}
