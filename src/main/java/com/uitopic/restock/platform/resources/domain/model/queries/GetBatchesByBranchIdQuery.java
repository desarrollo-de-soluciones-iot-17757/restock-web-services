package com.uitopic.restock.platform.resources.domain.model.queries;

/**
 * Query to retrieve batches by branch and optional custom supply within the resources bounded context.
 */
public record GetBatchesByBranchIdQuery(String branchId, String customSupplyId) {}
