package com.uitopic.restock.platform.planning.application.internal.queryservices;

import com.uitopic.restock.platform.planning.domain.model.aggregates.Product;
import com.uitopic.restock.platform.planning.domain.model.queries.GetProductByIdQuery;
import com.uitopic.restock.platform.planning.domain.model.queries.GetProductsAvailabilityQuery;
import com.uitopic.restock.platform.planning.domain.model.queries.GetProductsByAccountIdQuery;
import com.uitopic.restock.platform.planning.domain.repositories.ProductRepository;
import com.uitopic.restock.platform.planning.domain.services.InventoryStockPort;
import com.uitopic.restock.platform.planning.domain.services.ProductQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
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

    // Domain port for querying inventory stock data
    private final InventoryStockPort inventoryStockPort;

    // Constructor injection of the domain ports
    public ProductQueryServiceImpl(ProductRepository productRepository, InventoryStockPort inventoryStockPort) {
        this.productRepository = productRepository;
        this.inventoryStockPort = inventoryStockPort;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Map.Entry<Product, Integer>> handle(GetProductsAvailabilityQuery query) {
        log.debug("Calculating availability for accountId={}, branchId={}",
                query.accountId().getAccountId(), query.branchId());
        var products = productRepository.findByAccountId(query.accountId());
        return products.stream()
                .map(product -> Map.entry(product, calculateMaxAssemblable(product, query.branchId())))
                .toList();
    }

    private int calculateMaxAssemblable(Product product, String branchId) {
        var ingredients = product.getIngredients();
        if (ingredients == null || ingredients.isEmpty()) {
            return 0;
        }
        int min = Integer.MAX_VALUE;
        for (var ingredient : ingredients) {
            double totalStock = inventoryStockPort.getTotalStock(
                    ingredient.getCustomSupplyId(), branchId);
            double qtyRequired = ingredient.getQuantity();
            if (qtyRequired <= 0) {
                continue;
            }
            int assemblable = (int) (totalStock / qtyRequired);
            min = Math.min(min, assemblable);
        }
        return min == Integer.MAX_VALUE ? 0 : min;
    }
}
