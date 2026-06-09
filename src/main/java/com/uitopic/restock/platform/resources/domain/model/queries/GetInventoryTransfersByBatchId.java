package com.uitopic.restock.platform.resources.domain.model.queries;

/**
 * Query to retrieve inventory transfers by branch within the resources bounded context.
 */
public record GetInventoryTransfersByBatchId(String branchId) {}
