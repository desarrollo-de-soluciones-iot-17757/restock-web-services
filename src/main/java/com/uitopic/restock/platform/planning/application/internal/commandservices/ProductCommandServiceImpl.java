package com.uitopic.restock.platform.planning.application.internal.commandservices;

import com.uitopic.restock.platform.planning.domain.model.aggregates.Product;
import com.uitopic.restock.platform.planning.domain.model.commands.AddIngredientCommand;
import com.uitopic.restock.platform.planning.domain.model.commands.CreateProductCommand;
import com.uitopic.restock.platform.planning.domain.model.commands.RemoveIngredientCommand;
import com.uitopic.restock.platform.planning.domain.model.commands.UpdateProductCommand;
import com.uitopic.restock.platform.planning.domain.model.entities.Ingredient;
import com.uitopic.restock.platform.planning.domain.model.enums.ProductType;
import com.uitopic.restock.platform.planning.domain.model.events.ProductDeletedEvent;
import com.uitopic.restock.platform.planning.domain.repositories.ProductRepository;
import com.uitopic.restock.platform.planning.domain.services.CustomSupplyPricingPort;
import com.uitopic.restock.platform.planning.domain.services.ProductCommandService;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.ResourceStatus;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Application service implementing the write-side operations for the {@code planning} bounded context.
 *
 * <p>Orchestrates the following use cases:
 * <ul>
 *   <li><b>Create product</b> – validates SKU uniqueness per account, builds the aggregate,
 *       sets initial status to {@link ResourceStatus#ACTIVE} and persists.</li>
 *   <li><b>Update product</b> – partial update via the aggregate's {@code update()} method;
 *       re-validates SKU uniqueness only when the SKU actually changes.</li>
 *   <li><b>Add ingredient</b> – fetches unit price from the {@code resources} BC via
 *       {@link CustomSupplyPricingPort}, computes {@code totalCost = quantity × unitPrice},
 *       delegates mutation to the aggregate root, and persists.</li>
 *   <li><b>Remove ingredient</b> – delegates removal to the aggregate root and persists.</li>
 *   <li><b>Delete product</b> – removes the document and publishes
 *       {@link ProductDeletedEvent} for downstream consumers.</li>
 * </ul>
 */
@Slf4j
@Service
@Transactional
public class ProductCommandServiceImpl implements ProductCommandService {

    private final ProductRepository productRepository;
    private final CustomSupplyPricingPort pricingPort;
    private final ApplicationEventPublisher eventPublisher;

    public ProductCommandServiceImpl(ProductRepository productRepository,
                                     CustomSupplyPricingPort pricingPort,
                                     ApplicationEventPublisher eventPublisher) {
        this.productRepository = productRepository;
        this.pricingPort = pricingPort;
        this.eventPublisher = eventPublisher;
    }

    // -------------------------------------------------------------------------
    // Create
    // -------------------------------------------------------------------------

    /** {@inheritDoc} */
    @Override
    public Product handle(CreateProductCommand command) {
        AccountId accountId = new AccountId(command.accountId());

        if (productRepository.existsByAccountIdAndSku(accountId, command.sku())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "A product with SKU '" + command.sku() + "' already exists for this account.");
        }

        ProductType type = parseProductType(command.type());

        Product product = Product.builder()
                .accountId(accountId)
                .name(command.name())
                .description(command.description())
                .sku(command.sku())
                .type(type)
                .imageUrl(command.imageUrl())
                .sellingPrice(command.sellingPrice())
                .build();

        product.setResourceStatus(ResourceStatus.ACTIVE);

        Product saved = productRepository.save(product);
        log.info("Product created: id={}, sku={}, accountId={}",
                saved.getId(), saved.getSku(), accountId.getAccountId());
        return saved;
    }

    // -------------------------------------------------------------------------
    // Update
    // -------------------------------------------------------------------------

    /** {@inheritDoc} */
    @Override
    public Optional<Product> handle(UpdateProductCommand command) {
        return productRepository.findById(command.productId()).map(product -> {

            // Only re-validate SKU uniqueness when it actually changes
            String newSku = command.sku();
            if (newSku != null && !newSku.isBlank() && !newSku.equals(product.getSku())) {
                if (productRepository.existsByAccountIdAndSku(product.getAccountId(), newSku)) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "A product with SKU '" + newSku + "' already exists for this account.");
                }
            }

            product.update(
                    command.name(),
                    command.description(),
                    command.sku(),
                    command.imageUrl(),
                    command.sellingPrice()
            );

            Product saved = productRepository.save(product);
            log.info("Product updated: id={}", saved.getId());
            return saved;
        });
    }

    // -------------------------------------------------------------------------
    // Add ingredient
    // -------------------------------------------------------------------------

    /** {@inheritDoc} */
    @Override
    public Optional<Product> handle(AddIngredientCommand command) {
        return productRepository.findById(command.productId()).map(product -> {

            BigDecimal unitPrice = pricingPort.getUnitPrice(command.customSupplyId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                            "Custom supply not found or has no price: " + command.customSupplyId()));

            BigDecimal totalCost = unitPrice.multiply(BigDecimal.valueOf(command.quantity()));

            Ingredient ingredient = Ingredient.builder()
                    .id(new ObjectId().toHexString())
                    .productId(product.getId())
                    .customSupplyId(command.customSupplyId())
                    .quantity(command.quantity())
                    .totalCost(totalCost)
                    .build();

            try {
                product.addIngredient(ingredient);
            } catch (IllegalArgumentException ex) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
            }

            Product saved = productRepository.save(product);
            log.info("Ingredient added to product {}: customSupplyId={}, qty={}, totalCost={}",
                    product.getId(), command.customSupplyId(), command.quantity(), totalCost);
            return saved;
        });
    }

    // -------------------------------------------------------------------------
    // Remove ingredient
    // -------------------------------------------------------------------------

    /** {@inheritDoc} */
    @Override
    public Optional<Product> handle(RemoveIngredientCommand command) {
        return productRepository.findById(command.productId()).map(product -> {
            try {
                product.removeIngredient(command.customSupplyId());
            } catch (IllegalArgumentException ex) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
            }
            Product saved = productRepository.save(product);
            log.info("Ingredient removed from product {}: customSupplyId={}",
                    product.getId(), command.customSupplyId());
            return saved;
        });
    }

    // -------------------------------------------------------------------------
    // Delete
    // -------------------------------------------------------------------------

    /** {@inheritDoc} */
    @Override
    public void delete(String id) {
        productRepository.findById(id).ifPresentOrElse(
                product -> {
                    String accountId = product.getAccountId().getAccountId();
                    productRepository.deleteById(id);
                    eventPublisher.publishEvent(new ProductDeletedEvent(id, accountId));
                    log.info("Product deleted: id={}, accountId={}", id, accountId);
                },
                () -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Product not found: " + id);
                });
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private ProductType parseProductType(String raw) {
        try {
            return ProductType.valueOf(raw.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Invalid product type '" + raw + "'. Accepted values: RECIPE, KIT.");
        }
    }
}
