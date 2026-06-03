package com.uitopic.restock.platform.planning.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.planning.domain.model.aggregates.Product;
import com.uitopic.restock.platform.planning.infrastructure.persistence.mongodb.entities.ProductPersistenceEntity;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MongoDB repository for {@link Product} aggregates within the {@code planning} bounded context.
 *
 * <p>Extends Spring Data's {@link MongoRepository} to provide standard CRUD operations
 * and custom query methods for the {@code products} collection. Used exclusively by
 * {@link com.uitopic.restock.platform.planning.infrastructure.adapters.ProductRepositoryImpl}
 * as the underlying persistence mechanism.</p>
 */
@Repository
public interface ProductPersistenceRepository extends MongoRepository<ProductPersistenceEntity, String> {

    /**
     * Finds all products associated with the specified account ID.
     *
     * @param accountId the tenant account whose products are to be retrieved
     * @return a {@link List} of {@link Product} aggregates for that account
     */
    List<ProductPersistenceEntity> findByAccountId(AccountId accountId);

    /**
     * Checks whether a product with the given SKU already exists within the specified account.
     * Used to enforce SKU uniqueness per account before creating a new product.
     *
     * @param accountId the account scope for the uniqueness check
     * @param sku       the SKU to check
     * @return {@code true} if a product with that SKU exists in the account, {@code false} otherwise
     */
    boolean existsByAccountIdAndSku(AccountId accountId, String sku);
}
