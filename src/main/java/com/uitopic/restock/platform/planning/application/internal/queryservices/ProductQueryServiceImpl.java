package com.uitopic.restock.platform.planning.application.internal.queryservices;

import com.uitopic.restock.platform.planning.domain.model.aggregates.Product;
import com.uitopic.restock.platform.planning.domain.model.queries.GetProductByIdQuery;
import com.uitopic.restock.platform.planning.domain.model.queries.GetProductsByAccountIdQuery;
import com.uitopic.restock.platform.planning.domain.repositories.ProductRepository;
import com.uitopic.restock.platform.planning.domain.services.ProductQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Application service implementing the read-side operations for the {@code planning} bounded context.
 *
 * <p>Handles queries for {@link Product} aggregates using the {@link ProductRepository} domain port.</p>
 */
@Slf4j
@Service
public class ProductQueryServiceImpl implements ProductQueryService {

    // Domain port for accessing Product data
    private final ProductRepository productRepository;

    // Constructor injection of the ProductRepository domain port
    public ProductQueryServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Product> handle(GetProductByIdQuery query) {
        log.debug("Querying product by id: {}", query.productId());
        return productRepository.findById(query.productId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Product> handle(GetProductsByAccountIdQuery query) {
        log.debug("Querying all products for accountId: {}", query.accountId().getAccountId());
        return productRepository.findByAccountId(query.accountId());
    }
}
