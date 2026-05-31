package com.uitopic.restock.platform.resources.domain.services;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Batch;
import com.uitopic.restock.platform.resources.domain.model.queries.GetBatchesByCustomSupplyIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for handling batch-related queries. This interface defines methods for retrieving batch information based on specific criteria, such as custom supply ID or batch ID. Implementations of this service will provide the necessary logic to interact with the underlying data sources and return the requested batch information in a structured manner.
 */
public interface BatchQueryService {

    /**
     * Handles the query to retrieve batches based on a custom supply ID. This method is responsible for processing the GetBatchesByCustomSupplyIdQuery and returning a list of Batch objects that match the specified criteria. The implementation of this method will involve querying the underlying data source to find batches associated with the given custom supply ID and returning them in a structured format.
     *
     * @param query the query object containing the custom supply ID for which batches need to be retrieved. This object encapsulates the necessary information to perform the query, allowing for a clean separation of concerns and facilitating the retrieval of batch data based on specific criteria.
     * @return a list of Batch objects that match the criteria specified in the query, or an empty list if no matching batches are found. The returned list will contain the relevant batch information, such as batch ID, custom supply ID, quantity, and other pertinent details related to the batches associated with the specified custom supply ID.
     */
    List<Batch> handle(GetBatchesByCustomSupplyIdQuery query);

    /**
     * Finds a batch by its unique identifier. This method is essential for retrieving specific batch details based on the batch ID, allowing for accurate batch management and tracking. The implementation of this method will involve querying the underlying data source to find a batch that matches the provided ID and returning it in an Optional wrapper to handle cases where the batch may not be found.
     *
     * @param id the unique identifier of the batch to be retrieved. This parameter is crucial for identifying the specific batch that needs to be fetched from the data source, allowing for precise retrieval of batch information based on its unique ID.
     * @return an Optional containing the Batch object if found, or an empty Optional if no matching batch exists. The returned Batch object will contain relevant information about the batch, such as its ID, custom supply ID, quantity, and other pertinent details related to the batch. The use of Optional allows for graceful handling of cases where the batch may not be found in the data source, providing a clear indication of the presence or absence of the requested batch information.
     */
    Optional<Batch> findById(String id);
}
