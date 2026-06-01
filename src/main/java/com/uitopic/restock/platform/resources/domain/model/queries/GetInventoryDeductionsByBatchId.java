package com.uitopic.restock.platform.resources.domain.model.queries;

/**
 * Query to retrieve inventory deductions by branch within the resources bounded context.
 */
public record GetInventoryDeductionsByBatchId(String branchId) {}
