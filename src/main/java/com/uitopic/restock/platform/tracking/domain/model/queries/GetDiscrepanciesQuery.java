package com.uitopic.restock.platform.tracking.domain.model.queries;

import com.uitopic.restock.platform.tracking.domain.model.valueobjects.DiscrepancyStatus;

/**
 * Query used to retrieve discrepancies with optional filters.
 *
 * @param status optional discrepancy status filter
 */
public record GetDiscrepanciesQuery(DiscrepancyStatus status) {
}
