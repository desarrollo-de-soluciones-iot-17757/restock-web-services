package com.uitopic.restock.platform.resources.infrastructure.repositories;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Batch;
import com.uitopic.restock.platform.resources.domain.repositories.BatchRepository;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories.BatchMongoRepository;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the BatchRepository interface that interacts with a MongoDB database using the BatchMongoRepository. This class provides methods for saving, retrieving, and deleting Batch entities based on various criteria such as batch ID, custom supply ID, and account ID. By implementing the BatchRepository interface, this class abstracts away the underlying data access logic and allows for flexible interaction with the MongoDB database while maintaining a clean separation of concerns within the application architecture.
 */
@Repository
public class BatchRepositoryImpl implements BatchRepository {

    // The BatchRepositoryImpl class is an implementation of the BatchRepository interface, which provides methods for managing Batch entities in the context of inventory management. This implementation uses a BatchMongoRepository to interact with a MongoDB database, allowing for efficient storage and retrieval of Batch data. The repository provides methods for saving batches, finding batches by ID, custom supply ID, and account ID, as well as deleting batches by ID. By implementing the BatchRepository interface, this class ensures that the underlying data access logic is abstracted away from the rest of the application, promoting a clean separation of concerns and enabling easier testing and maintenance.
    private final BatchMongoRepository mongo;

    // Constructor injection of the BatchMongoRepository, which is responsible for interacting with the MongoDB database to perform CRUD operations on Batch entities. This allows the BatchRepositoryImpl to delegate data access operations to the MongoDB repository, ensuring a clean separation of concerns and enabling easier testing and maintenance of the repository implementation.
    public BatchRepositoryImpl(BatchMongoRepository mongo) {
        this.mongo = mongo;
    }

    /**
     * Saves a Batch entity to the MongoDB database. This method can be used for both creating new batches and updating existing ones. The implementation should handle the logic for determining whether to insert a new record or update an existing one based on the presence of an ID in the Batch entity. The saved Batch entity is returned, which may include an auto-generated ID if it was a new batch, or the updated Batch entity if it was an existing batch that was modified.
     *
     * @param batch the Batch entity to be saved, which may contain details such as the associated branch, custom supply, quantity, and other relevant information
     * @return the saved Batch entity, which may include an auto-generated ID if it was a new batch, or the updated Batch entity if it was an existing batch that was modified
     */
    @Override
    public Batch save(Batch batch) { return mongo.save(batch); }

    /**
     * Finds a Batch entity by its unique identifier. This method retrieves a Batch based on the provided ID, returning an Optional that contains the Batch if found, or an empty Optional if no Batch with the given ID exists in the repository. The ID is typically a string that uniquely identifies the batch within the system, and is used to locate and retrieve the corresponding Batch entity from the MongoDB database.
     *
     * @param id the unique identifier of the Batch to be retrieved, which is typically a string that uniquely identifies the batch within the system
     * @return an Optional containing the Batch entity if found, or an empty Optional if no Batch with the specified ID exists in the repository
     */
    @Override
    public Optional<Batch> findById(String id) { return mongo.findById(id); }

    /**
     * Retrieves all Batch entities from the MongoDB database that are associated with a specific custom supply ID. This method returns a list of all batches currently stored in the repository that are related to the specified custom supply ID, which can be useful for displaying inventory levels, auditing, or performing batch operations. The custom supply ID is used to track inventory levels and movements related to a specific supply, allowing for better inventory management and traceability of stock associated with that supply.
     *
     * @param id the custom supply ID for which to find batches, allowing retrieval of all batches associated with a specific supply, which can be useful for tracking inventory levels and movements related to that supply
     * @return a list of Batch entities associated with the specified custom supply ID, which may include batches from multiple branches or accounts that are related to the same supply
     */
    @Override public List<Batch> findByCustomSupplyId(String id) { return mongo.findByCustomSupplyId(id); }

    /**
     * Finds Batch entities from the MongoDB database that are associated with a specific account ID. This method allows retrieval of all batches related to a particular account, which can be useful for auditing and tracking inventory movements across different accounts. The account ID is typically a value object that encapsulates the unique identifier of an account within the system, and is used to locate and retrieve the corresponding Batch entities from the MongoDB database, providing insights into the inventory levels and transactions related to that account.
     *
     * @param accountId the ID of the account for which to find batches, which is typically a value object that encapsulates the unique identifier of an account within the system
     * @return a list of Batch entities associated with the specified account ID, which may include batches from multiple branches or supplies that are related to the same account, providing a comprehensive view of the inventory associated with that account
     */
    @Override public List<Batch> findByAccountId(AccountId accountId) { return mongo.findByAccountId(accountId); }

    /**
     * Deletes a Batch entity from the MongoDB database based on its unique identifier. This method removes the Batch with the specified ID from the repository, which can be used for cleaning up old or irrelevant batches, or for handling batch deletions as part of inventory management processes. The ID is typically a string that uniquely identifies the batch within the system, and is used to locate and remove the corresponding Batch entity from the MongoDB database.
     *
     * @param id the unique identifier of the Batch to be deleted, which is typically a string that uniquely identifies the batch within the system, and is used to locate and remove the corresponding Batch entity from the repository
     */
    @Override public void deleteById(String id) { mongo.deleteById(id); }
}
