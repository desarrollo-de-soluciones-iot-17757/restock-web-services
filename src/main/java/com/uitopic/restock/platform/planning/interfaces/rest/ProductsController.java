package com.uitopic.restock.platform.planning.interfaces.rest;

import com.uitopic.restock.platform.planning.domain.model.commands.RemoveIngredientCommand;
import com.uitopic.restock.platform.planning.domain.model.queries.GetProductByIdQuery;
import com.uitopic.restock.platform.planning.domain.model.queries.GetProductsByAccountIdQuery;
import com.uitopic.restock.platform.planning.domain.services.ProductCommandService;
import com.uitopic.restock.platform.planning.domain.services.ProductQueryService;
import com.uitopic.restock.platform.planning.interfaces.rest.resources.AddIngredientResource;
import com.uitopic.restock.platform.planning.interfaces.rest.resources.CreateProductResource;
import com.uitopic.restock.platform.planning.interfaces.rest.resources.ProductResource;
import com.uitopic.restock.platform.planning.interfaces.rest.resources.UpdateProductResource;
import com.uitopic.restock.platform.planning.interfaces.rest.transform.AddIngredientCommandFromResourceAssembler;
import com.uitopic.restock.platform.planning.interfaces.rest.transform.CreateProductCommandFromResourceAssembler;
import com.uitopic.restock.platform.planning.interfaces.rest.transform.ProductResourceFromEntityAssembler;
import com.uitopic.restock.platform.planning.interfaces.rest.transform.UpdateProductCommandFromResourceAssembler;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * REST controller exposing the {@code /api/v1/products} endpoints for the
 * {@code planning} bounded context.
 *
 * <table>
 *   <tr><th>Method</th><th>Path</th><th>Description</th></tr>
 *   <tr><td>POST</td>  <td>/api/v1/products</td>                                    <td>Create product</td></tr>
 *   <tr><td>GET</td>   <td>/api/v1/products/{productId}</td>                         <td>Get by ID</td></tr>
 *   <tr><td>GET</td>   <td>/api/v1/products?accountId=</td>                          <td>List by account</td></tr>
 *   <tr><td>PUT</td>   <td>/api/v1/products/{productId}</td>                         <td>Update product</td></tr>
 *   <tr><td>DELETE</td><td>/api/v1/products/{productId}</td>                         <td>Delete product</td></tr>
 *   <tr><td>POST</td>  <td>/api/v1/products/{productId}/ingredients</td>             <td>Add ingredient</td></tr>
 *   <tr><td>DELETE</td><td>/api/v1/products/{productId}/ingredients/{supplyId}</td>  <td>Remove ingredient</td></tr>
 * </table>
 */
@RestController
@RequestMapping(value = "/api/v1/products", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Products", description = "Product management – create and manage recipes and kits with their ingredients")
public class ProductsController {

    private final ProductCommandService commandService;
    private final ProductQueryService queryService;

    public ProductsController(ProductCommandService commandService,
                               ProductQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    // =========================================================================
    // COMMANDS
    // =========================================================================

    /**
     * Creates a new product (recipe or kit) for the given account.
     */
    @Operation(summary = "Create a product",
               description = "Creates a new RECIPE or KIT product. SKU must be unique per account.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error in request body"),
            @ApiResponse(responseCode = "409", description = "SKU already exists for this account"),
            @ApiResponse(responseCode = "422", description = "Invalid product type value")
    })
    @PostMapping
    public ResponseEntity<ProductResource> createProduct(
            @Valid @RequestBody CreateProductResource resource) {

        var command = CreateProductCommandFromResourceAssembler.toCommandFromResource(resource);
        var product = commandService.handle(command);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ProductResourceFromEntityAssembler.toResourceFromEntity(product));
    }

    /**
     * Updates the mutable fields of an existing product (partial update semantics:
     * any field left {@code null} in the body is unchanged).
     */
    @Operation(summary = "Update a product",
               description = "Partially updates a product. Null fields are ignored (keep current value).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "409", description = "New SKU conflicts with an existing product in this account")
    })
    @PutMapping("/{productId}")
    public ResponseEntity<ProductResource> updateProduct(
            @Parameter(description = "Product ID") @PathVariable String productId,
            @RequestBody UpdateProductResource resource) {

        var command = UpdateProductCommandFromResourceAssembler.toCommandFromResource(productId, resource);
        var product = commandService.handle(command)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Product not found: " + productId));
        return ResponseEntity.ok(ProductResourceFromEntityAssembler.toResourceFromEntity(product));
    }

    /**
     * Deletes a product by its ID. Also publishes a {@code ProductDeletedEvent}.
     */
    @Operation(summary = "Delete a product")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "Product ID") @PathVariable String productId) {
        commandService.delete(productId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Adds an ingredient to an existing product.
     * The server computes {@code totalCost = quantity × unitPrice} internally.
     */
    @Operation(summary = "Add an ingredient to a product",
               description = "Appends a custom supply as an ingredient. totalCost is calculated server-side from the supply's current unit price.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ingredient added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid quantity"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "409", description = "Ingredient with this customSupplyId already exists in the product"),
            @ApiResponse(responseCode = "422", description = "Custom supply not found or has no unit price configured")
    })
    @PostMapping("/{productId}/ingredients")
    public ResponseEntity<ProductResource> addIngredient(
            @Parameter(description = "Product ID") @PathVariable String productId,
            @Valid @RequestBody AddIngredientResource resource) {

        var command = AddIngredientCommandFromResourceAssembler.toCommandFromResource(productId, resource);
        var product = commandService.handle(command)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Product not found: " + productId));
        return ResponseEntity.ok(ProductResourceFromEntityAssembler.toResourceFromEntity(product));
    }

    /**
     * Removes a specific ingredient from a product identified by its {@code customSupplyId}.
     */
    @Operation(summary = "Remove an ingredient from a product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ingredient removed successfully"),
            @ApiResponse(responseCode = "404", description = "Product or ingredient not found")
    })
    @DeleteMapping("/{productId}/ingredients/{customSupplyId}")
    public ResponseEntity<ProductResource> removeIngredient(
            @Parameter(description = "Product ID") @PathVariable String productId,
            @Parameter(description = "Custom supply ID of the ingredient to remove")
            @PathVariable String customSupplyId) {

        var command = new RemoveIngredientCommand(productId, customSupplyId);
        var product = commandService.handle(command)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Product not found: " + productId));
        return ResponseEntity.ok(ProductResourceFromEntityAssembler.toResourceFromEntity(product));
    }

    // =========================================================================
    // QUERIES
    // =========================================================================

    /**
     * Retrieves a product by its unique identifier.
     */
    @Operation(summary = "Get a product by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResource> getProductById(
            @Parameter(description = "Product ID") @PathVariable String productId) {

        var query = new GetProductByIdQuery(productId);
        var product = queryService.handle(query)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Product not found: " + productId));
        return ResponseEntity.ok(ProductResourceFromEntityAssembler.toResourceFromEntity(product));
    }

    /**
     * Lists all products belonging to a given tenant account.
     */
    @Operation(summary = "List products by account",
               description = "Returns all products (recipes and kits) associated with the given accountId.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products retrieved (may be an empty list)"),
            @ApiResponse(responseCode = "400", description = "Missing or blank accountId parameter")
    })
    @GetMapping
    public ResponseEntity<List<ProductResource>> getProductsByAccount(
            @Parameter(description = "Tenant account ID", required = true)
            @RequestParam String accountId) {

        var query = new GetProductsByAccountIdQuery(new AccountId(accountId));
        var products = queryService.handle(query).stream()
                .map(ProductResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(products);
    }
}
