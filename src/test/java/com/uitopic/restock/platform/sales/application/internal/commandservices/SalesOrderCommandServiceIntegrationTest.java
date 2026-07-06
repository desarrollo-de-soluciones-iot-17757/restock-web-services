package com.uitopic.restock.platform.sales.application.internal.commandservices;

import com.uitopic.restock.platform.sales.application.internal.outboundservices.acl.ExternalPlanningService;
import com.uitopic.restock.platform.sales.application.internal.outboundservices.acl.ExternalResourcesService;
import com.uitopic.restock.platform.sales.domain.model.aggregates.SalesOrder;
import com.uitopic.restock.platform.sales.domain.model.commands.AddProductToOrderCommand;
import com.uitopic.restock.platform.sales.domain.model.commands.CancelSalesOrderCommand;
import com.uitopic.restock.platform.sales.domain.model.commands.CompleteSalesOrderCommand;
import com.uitopic.restock.platform.sales.domain.model.commands.CreateSalesOrderCommand;
import com.uitopic.restock.platform.sales.domain.model.valueobjects.BatchConsumption;
import com.uitopic.restock.platform.sales.domain.model.valueobjects.ProductType;
import com.uitopic.restock.platform.sales.domain.model.valueobjects.SalesOrderStatus;
import com.uitopic.restock.platform.sales.domain.repositories.SalesOrderRepository;
import com.uitopic.restock.platform.sales.domain.services.SalesOrderCommandService;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Integration test for the SalesOrder write path: real Spring context, real
 * MongoDB-backed persistence for the SalesOrder aggregate, and real domain
 * logic (SalesOrder / SalesOrderItem). Only the outbound ACLs to the other
 * bounded contexts (Planning and Resources) are mocked, since exercising
 * those would mean spinning up unrelated bounded contexts entirely.
 *
 * <p>This exercises the actual regression end-to-end: a custom supply sold
 * directly (SUPPLY) must have its own stock decremented on order completion,
 * exactly like a KIT does via its recipe ingredients — round-tripping through
 * real Mongo persistence in between every step, not just in-memory mocks.</p>
 */
@SpringBootTest
@ActiveProfiles("test")
class SalesOrderCommandServiceIntegrationTest {

    @Autowired
    private SalesOrderCommandService salesOrderCommandService;

    @Autowired
    private SalesOrderRepository salesOrderRepository;

    @MockitoBean
    private ExternalPlanningService externalPlanningService;

    @MockitoBean
    private ExternalResourcesService externalResourcesService;

    private static final String BRANCH_ID = "branch-integration-1";
    private static final String ACCOUNT_ID = "account-integration-1";

    @BeforeEach
    void setUp() {
        salesOrderRepository.findAll().forEach(o -> salesOrderRepository.deleteById(o.getId()));
    }

    @Test
    void fullFlow_sellingCustomSupplyDirectly_decrementsItsOwnStockOnCompletion() {
        // Arrange: create order and add a SUPPLY item directly (the exact "custom
        // supply sold standalone" scenario reported by the user).
        SalesOrder created = salesOrderCommandService.handle(
                new CreateSalesOrderCommand(new AccountId(ACCOUNT_ID), BRANCH_ID));
        assertNotNull(created.getId());

        salesOrderCommandService.handle(new AddProductToOrderCommand(
                created.getId(), "supply-cola-500ml", ProductType.SUPPLY,
                "Coca-Cola 500ml", BigDecimal.valueOf(3.5), "PEN", 4));

        when(externalResourcesService.resolveBatchConsumption(eq(BRANCH_ID), eq("supply-cola-500ml"), eq(4.0)))
                .thenReturn(new BatchConsumption("batch-cola-01", "supply-cola-500ml", 4.0));

        // Act
        SalesOrder completed = salesOrderCommandService.handle(new CompleteSalesOrderCommand(created.getId()));

        // Assert: stock was actually resolved/decremented for the supply itself,
        // never through the (irrelevant) kit-ingredients lookup.
        org.mockito.Mockito.verify(externalPlanningService, org.mockito.Mockito.never())
                .getProductIngredients(anyString());
        org.mockito.Mockito.verify(externalResourcesService, org.mockito.Mockito.times(1))
                .subtractBatchStock(BRANCH_ID, "batch-cola-01", 4.0);

        assertEquals(SalesOrderStatus.COMPLETED, completed.getStatus());
        assertEquals(1, completed.getAllBatchConsumptions().size());
        assertEquals("batch-cola-01", completed.getAllBatchConsumptions().get(0).batchId());

        // Assert: this is truly persisted, not just held in memory.
        Optional<SalesOrder> reloaded = salesOrderRepository.findById(completed.getId());
        assertTrue(reloaded.isPresent());
        assertEquals(SalesOrderStatus.COMPLETED, reloaded.get().getStatus());
        assertEquals(1, reloaded.get().getAllBatchConsumptions().size());
    }

    @Test
    void fullFlow_sellingKit_stillResolvesIngredientsAndDecrementsEachSupply() {
        SalesOrder created = salesOrderCommandService.handle(
                new CreateSalesOrderCommand(new AccountId(ACCOUNT_ID), BRANCH_ID));

        salesOrderCommandService.handle(new AddProductToOrderCommand(
                created.getId(), "kit-combo-familiar", ProductType.KIT,
                "Combo Familiar", BigDecimal.valueOf(29.9), "PEN", 1));

        when(externalPlanningService.getProductIngredients("kit-combo-familiar")).thenReturn(List.of(
                new ExternalPlanningService.IngredientRequirement("supply-bun", 2.0),
                new ExternalPlanningService.IngredientRequirement("supply-soda", 1.0)
        ));
        when(externalResourcesService.resolveBatchConsumption(eq(BRANCH_ID), eq("supply-bun"), eq(2.0)))
                .thenReturn(new BatchConsumption("batch-bun-01", "supply-bun", 2.0));
        when(externalResourcesService.resolveBatchConsumption(eq(BRANCH_ID), eq("supply-soda"), eq(1.0)))
                .thenReturn(new BatchConsumption("batch-soda-01", "supply-soda", 1.0));

        SalesOrder completed = salesOrderCommandService.handle(new CompleteSalesOrderCommand(created.getId()));

        org.mockito.Mockito.verify(externalPlanningService, org.mockito.Mockito.times(1))
                .getProductIngredients("kit-combo-familiar");
        org.mockito.Mockito.verify(externalResourcesService)
                .subtractBatchStock(BRANCH_ID, "batch-bun-01", 2.0);
        org.mockito.Mockito.verify(externalResourcesService)
                .subtractBatchStock(BRANCH_ID, "batch-soda-01", 1.0);

        assertEquals(2, completed.getAllBatchConsumptions().size());
    }

    @Test
    void fullFlow_mixedOrderWithKitAndStandaloneSupply_bothDecrementRealStockIndependently() {
        SalesOrder created = salesOrderCommandService.handle(
                new CreateSalesOrderCommand(new AccountId(ACCOUNT_ID), BRANCH_ID));

        salesOrderCommandService.handle(new AddProductToOrderCommand(
                created.getId(), "kit-2x1", ProductType.KIT, "Promo 2x1",
                BigDecimal.valueOf(15.0), "PEN", 1));
        SalesOrder afterFirstAdd = salesOrderRepository.findById(created.getId()).orElseThrow();

        salesOrderCommandService.handle(new AddProductToOrderCommand(
                afterFirstAdd.getId(), "supply-napkins", ProductType.SUPPLY, "Servilletas",
                BigDecimal.valueOf(1.0), "PEN", 10));

        when(externalPlanningService.getProductIngredients("kit-2x1"))
                .thenReturn(List.of(new ExternalPlanningService.IngredientRequirement("supply-burger", 2.0)));
        when(externalResourcesService.resolveBatchConsumption(eq(BRANCH_ID), eq("supply-burger"), eq(2.0)))
                .thenReturn(new BatchConsumption("batch-burger-01", "supply-burger", 2.0));
        when(externalResourcesService.resolveBatchConsumption(eq(BRANCH_ID), eq("supply-napkins"), eq(10.0)))
                .thenReturn(new BatchConsumption("batch-napkins-01", "supply-napkins", 10.0));

        SalesOrder completed = salesOrderCommandService.handle(new CompleteSalesOrderCommand(created.getId()));

        assertEquals(2, completed.getItems().size());
        assertEquals(2, completed.getAllBatchConsumptions().size());
        org.mockito.Mockito.verify(externalResourcesService).subtractBatchStock(BRANCH_ID, "batch-burger-01", 2.0);
        org.mockito.Mockito.verify(externalResourcesService).subtractBatchStock(BRANCH_ID, "batch-napkins-01", 10.0);
    }

    @Test
    void cancelOrder_persistsCancelledStatus() {
        SalesOrder created = salesOrderCommandService.handle(
                new CreateSalesOrderCommand(new AccountId(ACCOUNT_ID), BRANCH_ID));

        salesOrderCommandService.handle(new CancelSalesOrderCommand(created.getId()));

        SalesOrder reloaded = salesOrderRepository.findById(created.getId()).orElseThrow();
        assertEquals(SalesOrderStatus.CANCELLED, reloaded.getStatus());
    }
}
