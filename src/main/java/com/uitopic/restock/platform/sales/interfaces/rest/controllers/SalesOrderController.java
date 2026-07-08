package com.uitopic.restock.platform.sales.interfaces.rest.controllers;

import com.uitopic.restock.platform.sales.domain.model.aggregates.SalesOrder;
import com.uitopic.restock.platform.sales.domain.model.commands.CompleteSalesOrderCommand;
import com.uitopic.restock.platform.sales.domain.model.commands.CreateSalesOrderCommand;
import com.uitopic.restock.platform.sales.domain.model.queries.GetSalesOrderByIdQuery;
import com.uitopic.restock.platform.sales.domain.model.queries.GetSalesOrdersByAccountIdQuery;
import com.uitopic.restock.platform.sales.domain.model.queries.GetSalesOrdersByBranchIdQuery;
import com.uitopic.restock.platform.sales.domain.services.SalesOrderCommandService;
import com.uitopic.restock.platform.sales.domain.services.SalesOrderQueryService;
import com.uitopic.restock.platform.sales.interfaces.rest.resources.*;
import com.uitopic.restock.platform.sales.interfaces.rest.transform.*;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller for managing sales orders.
 * Exposes endpoints for creating, updating items, completing, canceling, and retrieving sales orders.
 */
@RestController
@RequestMapping(value = "/api/v1/sales-orders", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Sales Orders", description = "Operations related to Sales Orders management")
public class SalesOrderController {

    private final SalesOrderCommandService commandService;
    private final SalesOrderQueryService queryService;

    public SalesOrderController(SalesOrderCommandService commandService, SalesOrderQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    /**
     * Creates a new sales order for a specific account.
     *
     * @param accountId the identifier of the account creating the order
     * @param resource  the resource containing sales order details
     * @return the created sales order resource with a CREATED status
     */
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new sales order")
    public ResponseEntity<SalesOrderResource> create(@RequestParam String accountId, @Valid @RequestBody CreateSalesOrderResource resource) {

        CreateSalesOrderCommand command = CreateSalesOrderCommandFromResourceAssembler.toCommandFromResource(accountId, resource);
        SalesOrder salesOrder = commandService.handle(command);
        var response = SalesOrderResourceFromEntityAssembler.toResourceFromEntity(salesOrder);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Adds a product item to an existing sales order.
     *
     * @param orderId  the identifier of the sales order
     * @param resource the resource containing product data and quantity to add
     * @return the updated sales order resource with the added product
     */
    @PostMapping(value = "/{orderId}/items", consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Add a product to the sales order")
    public ResponseEntity<SalesOrderResource> addProduct(@PathVariable String orderId, @Valid @RequestBody AddProductToOrderResource resource) {
        var command = AddProductToOrderCommandFromResourceAssembler.toCommandFromResource(orderId, resource);
        var salesOrder = commandService.handle(command);
        return ResponseEntity.ok(SalesOrderResourceFromEntityAssembler.toResourceFromEntity(salesOrder));
    }

    /**
     * Removes a product item from an existing sales order.
     *
     * @param orderId the identifier of the sales order
     * @param itemId  the identifier of the item to remove
     * @return the updated sales order resource without the removed item
     */
    @DeleteMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Remove a product from the sales order")
    public ResponseEntity<SalesOrderResource> removeProduct(@PathVariable String orderId, @PathVariable String itemId) {
        var command = RemoveProductFromOrderCommandFromResourceAssembler.toCommandFromResource(orderId, itemId);
        var salesOrder = commandService.handle(command);
        return ResponseEntity.ok(SalesOrderResourceFromEntityAssembler.toResourceFromEntity(salesOrder));
    }

    /**
     * Updates the status of a sales order.
     * <p>
     * Only {@code COMPLETED} and {@code CANCELLED} are accepted transitions;
     * each dispatches to its own domain command under the hood (completing
     * consumes physical stock, cancelling does not).
     *
     * @param orderId  the identifier of the sales order
     * @param resource request body with the target status
     * @return the updated sales order resource
     */
    @PatchMapping(value = "/{orderId}/status", consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Update sales order status")
    public ResponseEntity<SalesOrderResource> updateStatus(
            @PathVariable String orderId,
            @Valid @RequestBody UpdateSalesOrderStatusResource resource) {

        SalesOrder salesOrder = switch (resource.status()) {
            case COMPLETED -> commandService.handle(new CompleteSalesOrderCommand(orderId));
            case CANCELLED -> {
                commandService.handle(CancelSalesOrderCommandFromResourceAssembler.toCommandFromResource(orderId));
                yield queryService.handle(new GetSalesOrderByIdQuery(orderId))
                        .orElseThrow(() -> new IllegalStateException("Sales order not found after cancellation: " + orderId));
            }
            default -> throw new IllegalArgumentException("Unsupported status transition: " + resource.status());
        };

        return ResponseEntity.ok(SalesOrderResourceFromEntityAssembler.toResourceFromEntity(salesOrder));
    }

    /**
     * Retrieves sales orders, optionally filtered by account or branch.
     * Exactly one of {@code accountId} or {@code branchId} is expected per call
     * (the Angular Sales Overview screen calls this with {@code accountId}).
     *
     * @param accountId the identifier of the account whose orders should be listed
     * @param branchId  the identifier of the branch whose orders should be listed
     * @return the list of matching sales order resources
     */
    @GetMapping
    @Operation(summary = "List sales orders by account or branch")
    public ResponseEntity<List<SalesOrderResource>> getOrders(
            @RequestParam(required = false) String accountId,
            @RequestParam(required = false) String branchId) {

        List<SalesOrder> orders;
        if (accountId != null && !accountId.isBlank()) {
            orders = queryService.handle(new GetSalesOrdersByAccountIdQuery(new AccountId(accountId)));
        } else if (branchId != null && !branchId.isBlank()) {
            orders = queryService.handle(new GetSalesOrdersByBranchIdQuery(branchId));
        } else {
            return ResponseEntity.badRequest().build();
        }

        var response = orders.stream()
                .map(SalesOrderResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves the details of a sales order by its identifier.
     *
     * @param orderId the identifier of the requested sales order
     * @return the sales order resource if found, or NOT FOUND status otherwise
     */
    @GetMapping("/{orderId}")
    @Operation(summary = "Get sales order details by ID")
    public ResponseEntity<SalesOrderResource> getById(@PathVariable String orderId) {
        return queryService.handle(new GetSalesOrderByIdQuery(orderId))
                .map(order -> ResponseEntity.ok(SalesOrderResourceFromEntityAssembler.toResourceFromEntity(order)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}