package com.uitopic.restock.platform.planning.infrastructure.adapters;

import com.uitopic.restock.platform.planning.domain.model.aggregates.Product;
import com.uitopic.restock.platform.planning.domain.repositories.ProductRepository;
import com.uitopic.restock.platform.planning.infrastructure.persistence.mongodb.assemblers.ProductPersistenceAssembler;
import com.uitopic.restock.platform.planning.infrastructure.persistence.mongodb.repositories.ProductPersistenceRepository;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link ProductRepository} that uses MongoDB for data storage
 * within the {@code planning} bounded context.
 *
 * <p>Acts as a bridge between the domain layer and the MongoDB persistence layer,
 * adapting {@link ProductPersistenceRepository} to the {@link ProductRepository} port.
 * This keeps the domain layer free of Spring Data dependencies.</p>
 */
@Repository
public class ProductRepositoryImpl implements ProductRepository {

    /** The underlying Spring Data MongoDB repository for {@link Product} documents. */
    private final ProductPersistenceRepository productPersistenceRepository;

    /**
     * Constructs a {@code ProductRepositoryImpl} with the given MongoDB repository.
     *
     * @param productPersistenceRepository the Spring Data MongoDB repository to delegate to
     */
    public ProductRepositoryImpl(ProductPersistenceRepository productPersistenceRepository) {
        this.productPersistenceRepository = productPersistenceRepository;
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Product> findById(String id) {
        return productPersistenceRepository.findById(id)
                .map(ProductPersistenceAssembler::toDomainFromPersistence);
    }

    /** {@inheritDoc} */
    @Override
    public List<Product> findByAccountId(AccountId accountId) {
        return productPersistenceRepository.findByAccountId(accountId)
                .stream()
                .map(ProductPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    /** {@inheritDoc} */
    @Override
    public boolean existsByAccountIdAndSku(AccountId accountId, String sku) {
        return productPersistenceRepository.existsByAccountIdAndSku(accountId, sku);
    }

    /** {@inheritDoc} */
    @Override
    public Product save(Product product) {
        var saved = productPersistenceRepository.save(ProductPersistenceAssembler.toPersistenceFromDomain(product));;
        return ProductPersistenceAssembler.toDomainFromPersistence(saved);
    }

    /** {@inheritDoc} */
    @Override
    public void deleteById(String id) {
        productPersistenceRepository.deleteById(id);
    }
}
