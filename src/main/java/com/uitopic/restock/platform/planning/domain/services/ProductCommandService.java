package com.uitopic.restock.platform.planning.domain.services;

import com.uitopic.restock.platform.planning.domain.model.aggregates.Product;
import com.uitopic.restock.platform.planning.domain.model.commands.AddIngredientCommand;
import com.uitopic.restock.platform.planning.domain.model.commands.CreateProductCommand;
import com.uitopic.restock.platform.planning.domain.model.commands.RemoveIngredientCommand;
import com.uitopic.restock.platform.planning.domain.model.commands.UpdateProductCommand;

import java.util.Optional;

/**
 * Domain service interface defining the command contract for
 * {@link Product} aggregate operations within the {@code planning} bounded context.
 *
 * <p>Declares write-side operations: creation, update, ingredient management, and deletion.
 * Implementations live in the application layer
 * ({@link com.uitopic.restock.platform.planning.application.internal.commandservices.ProductCommandServiceImpl}).</p>
 */
public interface ProductCommandService {

    /**
     * Handles the creation of a new product for the account specified in the command.
     * Validates SKU uniqueness within the account before persisting.
     *
     * @param command the command containing all data required to create the product
     * @return the newly created and persisted {@link Product} aggregate
     * @throws org.springframework.web.server.ResponseStatusException with 409 if SKU already exists
     */
    Product handle(CreateProductCommand command);

    /**
     * Updates the mutable fields of an existing product.
     * Fields set to {@code null} in the command are left unchanged.
     *
     * @param command the command containing the product ID and updated field values
     * @return an {@link Optional} containing the updated {@link Product}, or empty if not found
     * @throws org.springframework.web.server.ResponseStatusException with 409 if the new SKU
     *         conflicts with another product in the same account
     */
    Optional<Product> handle(UpdateProductCommand command);

    /**
     * Adds an ingredient to an existing product.
     * The {@code totalCost} is calculated internally using the pricing port.
     *
     * @param command the command containing the product ID, supply ID, and quantity
     * @return an {@link Optional} containing the updated {@link Product}, or empty if not found
     * @throws org.springframework.web.server.ResponseStatusException with 422 if the custom supply
     *         is not found or has no price, or 409 if the ingredient already exists in the product
     */
    Optional<Product> handle(AddIngredientCommand command);

    /**
     * Removes an ingredient from an existing product.
     *
     * @param command the command containing the product ID and the custom supply ID to remove
     * @return an {@link Optional} containing the updated {@link Product}, or empty if not found
     */
    Optional<Product> handle(RemoveIngredientCommand command);

    /**
     * Deletes a product by its unique identifier.
     * Publishes a {@link com.uitopic.restock.platform.planning.domain.model.events.ProductDeletedEvent}
     * upon successful deletion.
     *
     * @param id the unique identifier of the product to delete
     * @throws org.springframework.web.server.ResponseStatusException with 404 if not found
     */
    void delete(String id);
}
