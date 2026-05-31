package com.uitopic.restock.platform.resources.domain.repositories;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Batch;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Batch entities, providing methods for saving, retrieving, and deleting batches based on various criteria such as batch ID, branch ID, custom supply ID, and account ID. This interface abstracts the underlying data storage mechanism and allows for flexible implementations that can interact with different types of databases or persistence layers.
 */
public interface BatchRepository {

    /**
     * Saves a Batch entity to the repository. This method can be used for both creating new batches and updating existing ones. The implementation should handle the logic for determining whether to insert a new record or update an existing one based on the presence of an ID in the Batch entity.
     *
     * @param batch the Batch entity to be saved, which may contain details such as the associated branch, custom supply, quantity, and other relevant information
     * @return the saved Batch entity, which may include an auto-generated ID if it was a new batch, or the updated Batch entity if it was an existing batch that was modified
     */
    Batch save(Batch batch);

    /**
     * Finds a Batch entity by its unique identifier. This method retrieves a Batch based on the provided ID, returning an Optional that contains the Batch if found, or an empty Optional if no Batch with the given ID exists in the repository.
     *
     * @param id the unique identifier of the Batch to be retrieved, which is typically a string that uniquely identifies the batch within the system
     * @return an Optional containing the Batch entity if found, or an empty Optional if no Batch with the specified ID exists in the repository
     */
    Optional<Batch> findById(String id);

    /**
     * Retrieves all Batch entities from the repository. This method returns a list of all batches currently stored in the repository, which can be useful for displaying inventory levels, auditing, or performing batch operations.
     *
     * @param customSupplyId the custom supply ID for which to find batches, allowing retrieval of all batches associated with a specific supply, which can be useful for tracking inventory levels and movements related to that supply
     * @return a list of Batch entities associated with the specified custom supply ID, which may include batches from multiple branches or accounts that are related to the same supply
     */
    List<Batch> findByCustomSupplyId(String customSupplyId);

    /**
     * Finds Batch entities by account ID, allowing retrieval of all batches associated with a specific account. This method is useful for auditing and tracking inventory movements across different accounts, providing insights into the inventory levels and transactions related to a particular account.
     *
     * @param accountId the ID of the account for which to find batches, which is typically a value object that encapsulates the unique identifier of an account within the system
     * @return a list of Batch entities associated with the specified account ID, which may include batches from multiple branches or supplies that are related to the same account, providing a comprehensive view of the inventory associated with that account
     */
    List<Batch> findByAccountId(AccountId accountId);

    /**
     * Deletes a Batch entity from the repository based on its unique identifier. This method removes the Batch with the specified ID from the repository, which can be used for cleaning up old or irrelevant batches, or for handling batch deletions as part of inventory management processes.
     *
     * @param id the unique identifier of the Batch to be deleted, which is typically a string that uniquely identifies the batch within the system, and is used to locate and remove the corresponding Batch entity from the repository
     */
    void deleteById(String id);
}
