package com.uitopic.restock.platform.planning.domain.repositories;

import com.uitopic.restock.platform.planning.domain.model.aggregates.Product;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;

import java.util.List;
import java.util.Optional;

/**
 * Domain repository port for {@link Product} aggregate persistence within the
 * {@code planning} bounded context.
 *
 * <p>Defines the contract for storing and retrieving products, decoupling the domain
 * layer from MongoDB infrastructure. The implementation is provided by
 * {@link com.uitopic.restock.platform.planning.infrastructure.adapters.ProductRepositoryImpl}.</p>
 */
public interface ProductRepository {

    /**
     * Finds a product by its unique identifier.
     *
     * @param id the product's unique identifier
     * @return an {@link Optional} containing the {@link Product} if found, or empty otherwise
     */
    Optional<Product> findById(String id);

    /**
     * Finds all products belonging to the specified account.
     *
     * @param accountId the tenant account whose products are to be retrieved
     * @return a {@link List} of {@link Product} aggregates for that account
     */
    List<Product> findByAccountId(AccountId accountId);

    /**
     * Checks whether a product with the given SKU already exists within the specified account.
     * Used to enforce SKU uniqueness per account before creating a new product.
     *
     * @param accountId the account scope for the uniqueness check
     * @param sku       the SKU to check
     * @return {@code true} if a product with that SKU exists in the account, {@code false} otherwise
     */
    boolean existsByAccountIdAndSku(AccountId accountId, String sku);

    /**
     * Persists a {@link Product} aggregate. Creates a new document if the product has no ID,
     * or updates the existing document otherwise.
     *
     * @param product the product to save
     * @return the saved product, including any auto-generated ID
     */
    Product save(Product product);

    /**
     * Removes a product document from the store by its unique identifier.
     *
     * @param id the unique identifier of the product to delete
     */
    void deleteById(String id);
}
