package com.uitopic.restock.platform.sales.interfaces.rest.controllers;

import com.uitopic.restock.platform.sales.domain.model.aggregates.SalesOrder;
import com.uitopic.restock.platform.sales.domain.model.commands.CompleteSalesOrderCommand;
import com.uitopic.restock.platform.sales.domain.model.commands.CreateSalesOrderCommand;
import com.uitopic.restock.platform.sales.domain.model.queries.GetSalesOrderByIdQuery;
import com.uitopic.restock.platform.sales.domain.services.SalesOrderCommandService;
import com.uitopic.restock.platform.sales.domain.services.SalesOrderQueryService;
import com.uitopic.restock.platform.sales.interfaces.rest.resources.*;
import com.uitopic.restock.platform.sales.interfaces.rest.transform.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping(value = "/{orderId}/items/add", consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Add a product to the sales order")
    public ResponseEntity<SalesOrderResource> addProduct(@PathVariable String orderId, @Valid @RequestBody AddProductToOrderResource resource) {
        var command = AddProductToOrderCommandFromResourceAssembler.toCommandFromResource(orderId, resource);
        var salesOrder = commandService.handle(command);
        return ResponseEntity.ok(SalesOrderResourceFromEntityAssembler.toResourceFromEntity(salesOrder));
    }

    /**
     * Completes a sales order processing.
     *
     * @param orderId the identifier of the sales order to complete
     * @return a success message confirmation
     */
    @PatchMapping("/{orderId}/complete")
    @Operation(summary = "Complete a sales order")
    public ResponseEntity<String> complete(@PathVariable String orderId) {
        var command = new CompleteSalesOrderCommand(orderId);
        commandService.handle(command);

        return ResponseEntity.ok("Sales order completed successfully");
    }

    /**
     * Cancels an existing sales order.
     *
     * @param orderId the identifier of the sales order to cancel
     * @return a NO CONTENT status upon successful cancellation
     */
    @PatchMapping("/{orderId}/cancel")
    @Operation(summary = "Cancel a sales order")
    public ResponseEntity<Void> cancel(@PathVariable String orderId) {
        var command = CancelSalesOrderCommandFromResourceAssembler.toCommandFromResource(orderId);
        commandService.handle(command);
        return ResponseEntity.noContent().build();
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