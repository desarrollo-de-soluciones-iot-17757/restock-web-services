package com.uitopic.restock.platform.analytics.interfaces.rest.transform;

import com.uitopic.restock.platform.analytics.domain.model.commands.RegisterMetricCommand;
import com.uitopic.restock.platform.analytics.domain.model.valueobjects.DateRange;
import com.uitopic.restock.platform.analytics.domain.model.valueobjects.MetricCategory;
import com.uitopic.restock.platform.analytics.domain.model.valueobjects.MetricType;
import com.uitopic.restock.platform.analytics.interfaces.rest.resources.CreateMetricResource;

/** Assembler to convert CreateMetricResource into RegisterMetricCommand. */
public class CreateMetricCommandFromResourceAssembler {

    private CreateMetricCommandFromResourceAssembler() {
        throw new IllegalStateException("Utility class");
    }

    /** Converts a CreateMetricResource to a RegisterMetricCommand. */
    public static RegisterMetricCommand toCommandFromResource(CreateMetricResource resource) {
        if (resource == null) return null;

        return new RegisterMetricCommand(
                MetricCategory.valueOf(resource.category().toUpperCase()),
                MetricType.valueOf(resource.type().toUpperCase()),
                new DateRange(resource.dateRangeStart(), resource.dateRangeEnd()),
                resource.accountId()
        );
    }
}
