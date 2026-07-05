package com.uitopic.restock.platform.tracking.domain.services;

import com.uitopic.restock.platform.tracking.domain.model.entities.Discrepancy;
import com.uitopic.restock.platform.tracking.domain.model.queries.GetDiscrepanciesQuery;
import com.uitopic.restock.platform.tracking.domain.model.queries.GetDiscrepancyByIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Application service contract for discrepancy read operations.
 */
public interface DiscrepancyQueryService {
    /**
     * Retrieves discrepancies using optional filters.
     *
     * @param query query with optional filters
     * @return list of matching discrepancies
     */
    List<Discrepancy> handle(GetDiscrepanciesQuery query);

    /**
     * Retrieves a discrepancy by its identifier.
     *
     * @param query query with discrepancy identifier
     * @return optional discrepancy if found
     */
    Optional<Discrepancy> handle(GetDiscrepancyByIdQuery query);
}
