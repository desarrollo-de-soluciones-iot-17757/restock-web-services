package com.uitopic.restock.platform.planning.infrastructure.repositories;

import com.uitopic.restock.platform.planning.domain.model.aggregates.Product;
import com.uitopic.restock.platform.planning.domain.repositories.ProductRepository;
import com.uitopic.restock.platform.planning.infrastructure.persistence.mongodb.repositories.ProductMongoRepository;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link ProductRepository} that uses MongoDB for data storage
 * within the {@code planning} bounded context.
 *
 * <p>Acts as a bridge between the domain layer and the MongoDB persistence layer,
 * adapting {@link ProductMongoRepository} to the {@link ProductRepository} port.
 * This keeps the domain layer free of Spring Data dependencies.</p>
 */
@Repository
public class ProductRepositoryImpl implements ProductRepository {

    /** The underlying Spring Data MongoDB repository for {@link Product} documents. */
    private final ProductMongoRepository productMongoRepository;

    /**
     * Constructs a {@code ProductRepositoryImpl} with the given MongoDB repository.
     *
     * @param productMongoRepository the Spring Data MongoDB repository to delegate to
     */
    public ProductRepositoryImpl(ProductMongoRepository productMongoRepository) {
        this.productMongoRepository = productMongoRepository;
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Product> findById(String id) {
        return productMongoRepository.findById(id);
    }

    /** {@inheritDoc} */
    @Override
    public List<Product> findByAccountId(AccountId accountId) {
        return productMongoRepository.findByAccountId(accountId);
    }

    /** {@inheritDoc} */
    @Override
    public boolean existsByAccountIdAndSku(AccountId accountId, String sku) {
        return productMongoRepository.existsByAccountIdAndSku(accountId, sku);
    }

    /** {@inheritDoc} */
    @Override
    public Product save(Product product) {
        return productMongoRepository.save(product);
    }

    /** {@inheritDoc} */
    @Override
    public void deleteById(String id) {
        productMongoRepository.deleteById(id);
    }
}
