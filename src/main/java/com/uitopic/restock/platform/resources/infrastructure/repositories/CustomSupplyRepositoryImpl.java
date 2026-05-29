package com.uitopic.restock.platform.resources.infrastructure.repositories;

import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.resources.domain.repositories.CustomSupplyRepository;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories.CustomSupplyMongoRepository;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Implementation of the CustomSupplyRepository interface that interacts with the MongoDB database to perform operations related to CustomSupply aggregates.
 * This class uses the CustomSupplyMongoRepository to fetch data from the database and convert it into domain aggregates.
 */
@Repository
public class CustomSupplyRepositoryImpl implements CustomSupplyRepository {

    // Reference to the CustomSupplyMongoRepository for database operations
    private final CustomSupplyMongoRepository customSupplyMongoRepository;

    // Constructor injection of the CustomSupplyMongoRepository
    public CustomSupplyRepositoryImpl(CustomSupplyMongoRepository customSupplyMongoRepository) {
        this.customSupplyMongoRepository = customSupplyMongoRepository;
    }

    /**
     * Finds a list of CustomSupply aggregates by the given account ID.
     *
     * @param accountId the account ID for which to fetch the custom supplies
     * @return a list of CustomSupply aggregates that are associated with the specified account ID
     */
    @Override
    public List<CustomSupply> findByAccountId(AccountId accountId) {
        return customSupplyMongoRepository.findByAccountId(accountId);
    }
}
