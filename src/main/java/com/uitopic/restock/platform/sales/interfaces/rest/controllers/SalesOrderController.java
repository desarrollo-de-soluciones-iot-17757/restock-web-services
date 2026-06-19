package com.uitopic.restock.platform.sales.interfaces.rest.controllers;

import com.uitopic.restock.platform.sales.domain.model.aggregates.SalesOrder;
import com.uitopic.restock.platform.sales.domain.model.commands.CreateSalesOrderCommand;
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

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new sales order")
    public ResponseEntity<SalesOrderResource> create(@RequestParam String accountId, @Valid @RequestBody CreateSalesOrderResource resource) {

        CreateSalesOrderCommand command = CreateSalesOrderCommandFromResourceAssembler.toCommandFromResource(accountId, resource);
        SalesOrder salesOrder = commandService.handle(command);
        var response = SalesOrderResourceFromEntityAssembler.toResourceFromEntity(salesOrder);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}