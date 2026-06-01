package com.uitopic.restock.platform.resources.domain.model.queries;

/**
 * Query object for retrieving batches based on a custom supply ID. This query is used to fetch all batches that are associated with a specific custom supply ID, allowing for efficient retrieval of batch information related to a particular supply. The query encapsulates the necessary parameters required to perform the search, ensuring that the query handling logic can process the request effectively.
 *
 * @param customSupplyId the unique identifier of the custom supply for which batches are being retrieved. This parameter is essential for filtering the batches based on their association with the specified custom supply ID.
 */
public record GetBatchesByCustomSupplyIdQuery(String customSupplyId) {}
