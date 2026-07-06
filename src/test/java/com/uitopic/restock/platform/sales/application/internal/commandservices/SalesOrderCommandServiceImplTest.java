package com.uitopic.restock.platform.sales.application.internal.commandservices;

import com.uitopic.restock.platform.sales.application.internal.outboundservices.acl.ExternalPlanningService;
import com.uitopic.restock.platform.sales.application.internal.outboundservices.acl.ExternalResourcesService;
import com.uitopic.restock.platform.sales.domain.exceptions.SalesOrderNotFoundException;
import com.uitopic.restock.platform.sales.domain.model.aggregates.SalesOrder;
import com.uitopic.restock.platform.sales.domain.model.commands.*;
import com.uitopic.restock.platform.sales.domain.model.entities.SalesOrderItem;
import com.uitopic.restock.platform.sales.domain.model.valueobjects.BatchConsumption;
import com.uitopic.restock.platform.sales.domain.model.valueobjects.ProductType;
import com.uitopic.restock.platform.sales.domain.model.valueobjects.SalesOrderStatus;
import com.uitopic.restock.platform.sales.domain.repositories.SalesOrderRepository;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link SalesOrderCommandServiceImpl}.
 *
 * <p>The core regression covered here: completing an order used to always
 * resolve stock via {@code ExternalPlanningService#getProductIngredients},
 * which only makes sense for KIT/RECIPE items (products with a bill of
 * materials). A SUPPLY item sold directly at the POS has no "ingredients" —
 * its own productId IS the customSupplyId — so that lookup silently returned
 * an empty list and no batch was ever decremented. These tests assert that
 * SUPPLY items now decrement their own batch directly, while KIT/RECIPE items
 * keep going through the ingredients lookup as before.</p>
 */
class SalesOrderCommandServiceImplTest {

    @Mock
    private SalesOrderRepository salesOrderRepository;

    @Mock
    private ExternalResourcesService externalResourcesService;

    @Mock
    private ExternalPlanningService externalPlanningService;

    private SalesOrderCommandServiceImpl commandService;

    private static final String BRANCH_ID = "branch-123";
    private static final String ORDER_ID = "order-123";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        commandService = new SalesOrderCommandServiceImpl(
                salesOrderRepository, externalResourcesService, externalPlanningService);
    }

    private SalesOrder pendingOrderWith(SalesOrderItem... items) {
        SalesOrder order = new SalesOrder(new AccountId("account-123"), BRANCH_ID);
        for (SalesOrderItem item : items) {
            order.addItem(item);
        }
        return order;
    }

    private SalesOrderItem supplyItem(String supplyId, int quantity) {
        return new SalesOrderItem(
                supplyId, ProductType.SUPPLY, "Coca-Cola 500ml",
                new Money(BigDecimal.TEN, "PEN"), quantity);
    }

    private SalesOrderItem kitItem(String kitId, int quantity) {
        return new SalesOrderItem(
                kitId, ProductType.KIT, "Combo Familiar",
                new Money(BigDecimal.valueOf(25), "PEN"), quantity);
    }

    // ==================== CompleteSalesOrderCommand: SUPPLY items ====================

    @Test
    void completeOrder_withSupplyItem_subtractsStockDirectlyUsingProductIdAsSupplyId() {
        // Arrange
        SalesOrderItem item = supplyItem("supply-abc", 3);
        SalesOrder order = pendingOrderWith(item);
        when(salesOrderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));
        when(salesOrderRepository.save(any(SalesOrder.class))).thenAnswer(inv -> inv.getArgument(0));

        BatchConsumption expectedConsumption = new BatchConsumption("batch-1", "supply-abc", 3.0);
        when(externalResourcesService.resolveBatchConsumption(BRANCH_ID, "supply-abc", 3.0))
                .thenReturn(expectedConsumption);

        // Act
        SalesOrder result = commandService.handle(new CompleteSalesOrderCommand(ORDER_ID));

        // Assert: stock was resolved and subtracted directly for the supply, quantity == item quantity
        verify(externalResourcesService, times(1))
                .resolveBatchConsumption(BRANCH_ID, "supply-abc", 3.0);
        verify(externalResourcesService, times(1))
                .subtractBatchStock(BRANCH_ID, "batch-1", 3.0);

        // The "ingredients" lookup (which only applies to kits/recipes) must never be
        // consulted for a directly-sold supply — that's precisely the bug being fixed.
        verify(externalPlanningService, never()).getProductIngredients(anyString());

        assertEquals(1, result.getAllBatchConsumptions().size());
        assertEquals("batch-1", result.getAllBatchConsumptions().get(0).batchId());
        assertEquals(SalesOrderStatus.COMPLETED, result.getStatus());
    }

    @Test
    void completeOrder_withMultipleUnitsOfSupply_requestsTotalQuantityNotJustOne() {
        SalesOrderItem item = supplyItem("supply-xyz", 5);
        SalesOrder order = pendingOrderWith(item);
        when(salesOrderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));
        when(salesOrderRepository.save(any(SalesOrder.class))).thenAnswer(inv -> inv.getArgument(0));
        when(externalResourcesService.resolveBatchConsumption(eq(BRANCH_ID), eq("supply-xyz"), eq(5.0)))
                .thenReturn(new BatchConsumption("batch-9", "supply-xyz", 5.0));

        commandService.handle(new CompleteSalesOrderCommand(ORDER_ID));

        ArgumentCaptor<Double> quantityCaptor = ArgumentCaptor.forClass(Double.class);
        verify(externalResourcesService).resolveBatchConsumption(eq(BRANCH_ID), eq("supply-xyz"), quantityCaptor.capture());
        assertEquals(5.0, quantityCaptor.getValue());
    }

    // ==================== CompleteSalesOrderCommand: KIT/RECIPE items ====================

    @Test
    void completeOrder_withKitItem_stillResolvesIngredientsAndSubtractsPerIngredient() {
        SalesOrderItem item = kitItem("kit-1", 2);
        SalesOrder order = pendingOrderWith(item);
        when(salesOrderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));
        when(salesOrderRepository.save(any(SalesOrder.class))).thenAnswer(inv -> inv.getArgument(0));

        when(externalPlanningService.getProductIngredients("kit-1")).thenReturn(List.of(
                new ExternalPlanningService.IngredientRequirement("supply-bun", 2.0),
                new ExternalPlanningService.IngredientRequirement("supply-patty", 1.0)
        ));
        when(externalResourcesService.resolveBatchConsumption(BRANCH_ID, "supply-bun", 4.0))
                .thenReturn(new BatchConsumption("batch-bun", "supply-bun", 4.0));
        when(externalResourcesService.resolveBatchConsumption(BRANCH_ID, "supply-patty", 2.0))
                .thenReturn(new BatchConsumption("batch-patty", "supply-patty", 2.0));

        SalesOrder result = commandService.handle(new CompleteSalesOrderCommand(ORDER_ID));

        verify(externalPlanningService, times(1)).getProductIngredients("kit-1");
        verify(externalResourcesService).subtractBatchStock(BRANCH_ID, "batch-bun", 4.0);
        verify(externalResourcesService).subtractBatchStock(BRANCH_ID, "batch-patty", 2.0);
        assertEquals(2, result.getAllBatchConsumptions().size());
    }

    @Test
    void completeOrder_withKitThatHasNoIngredients_completesWithoutConsumptions() {
        SalesOrderItem item = kitItem("kit-empty", 1);
        SalesOrder order = pendingOrderWith(item);
        when(salesOrderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));
        when(salesOrderRepository.save(any(SalesOrder.class))).thenAnswer(inv -> inv.getArgument(0));
        when(externalPlanningService.getProductIngredients("kit-empty")).thenReturn(List.of());

        SalesOrder result = commandService.handle(new CompleteSalesOrderCommand(ORDER_ID));

        verify(externalResourcesService, never()).subtractBatchStock(anyString(), anyString(), anyDouble());
        assertTrue(result.getAllBatchConsumptions().isEmpty());
        assertEquals(SalesOrderStatus.COMPLETED, result.getStatus());
    }

    // ==================== CompleteSalesOrderCommand: mixed order ====================

    @Test
    void completeOrder_withBothKitAndSupplyItems_handlesEachAccordingToItsOwnType() {
        SalesOrderItem kit = kitItem("kit-2", 1);
        SalesOrderItem supply = supplyItem("supply-standalone", 2);
        SalesOrder order = pendingOrderWith(kit, supply);
        when(salesOrderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));
        when(salesOrderRepository.save(any(SalesOrder.class))).thenAnswer(inv -> inv.getArgument(0));

        when(externalPlanningService.getProductIngredients("kit-2"))
                .thenReturn(List.of(new ExternalPlanningService.IngredientRequirement("supply-cheese", 1.0)));
        when(externalResourcesService.resolveBatchConsumption(BRANCH_ID, "supply-cheese", 1.0))
                .thenReturn(new BatchConsumption("batch-cheese", "supply-cheese", 1.0));
        when(externalResourcesService.resolveBatchConsumption(BRANCH_ID, "supply-standalone", 2.0))
                .thenReturn(new BatchConsumption("batch-standalone", "supply-standalone", 2.0));

        SalesOrder result = commandService.handle(new CompleteSalesOrderCommand(ORDER_ID));

        // Kit went through the ingredients lookup exactly once (for itself only)...
        verify(externalPlanningService, times(1)).getProductIngredients("kit-2");
        verify(externalPlanningService, never()).getProductIngredients("supply-standalone");
        // ...and both items ended up decrementing real stock.
        verify(externalResourcesService).subtractBatchStock(BRANCH_ID, "batch-cheese", 1.0);
        verify(externalResourcesService).subtractBatchStock(BRANCH_ID, "batch-standalone", 2.0);
        assertEquals(2, result.getAllBatchConsumptions().size());
    }

    @Test
    void completeOrder_orderNotFound_throwsException() {
        when(salesOrderRepository.findById(ORDER_ID)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> commandService.handle(new CompleteSalesOrderCommand(ORDER_ID)));
        verify(salesOrderRepository, never()).save(any());
    }

    // ==================== Other commands (baseline coverage) ====================

    @Test
    void createOrder_savesNewPendingOrder() {
        when(salesOrderRepository.save(any(SalesOrder.class))).thenAnswer(inv -> inv.getArgument(0));

        SalesOrder result = commandService.handle(new CreateSalesOrderCommand(new AccountId("account-1"), BRANCH_ID));

        assertNotNull(result);
        assertEquals(SalesOrderStatus.PENDING, result.getStatus());
        verify(salesOrderRepository, times(1)).save(any(SalesOrder.class));
    }

    @Test
    void addProductToOrder_addsSupplyTypeItemCorrectly() {
        SalesOrder order = pendingOrderWith();
        // findById is called with the hex-normalized version of the order id inside handle();
        // for a plain non-ObjectId string this still round-trips through ObjectId.toHexString().
        var objectIdOrder = new org.bson.types.ObjectId();
        String orderId = objectIdOrder.toHexString();
        when(salesOrderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(salesOrderRepository.save(any(SalesOrder.class))).thenAnswer(inv -> inv.getArgument(0));

        AddProductToOrderCommand command = new AddProductToOrderCommand(
                orderId, "supply-loose", ProductType.SUPPLY, "Loose Item",
                BigDecimal.valueOf(4.5), "PEN", 2);

        SalesOrder result = commandService.handle(command);

        assertEquals(1, result.getItems().size());
        assertEquals(ProductType.SUPPLY, result.getItems().get(0).getProductType());
        assertEquals("supply-loose", result.getItems().get(0).getProductId());
    }

    @Test
    void removeProductFromOrder_removesExistingItem() {
        SalesOrderItem item = supplyItem("supply-1", 1);
        SalesOrder order = pendingOrderWith(item);
        when(salesOrderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));
        when(salesOrderRepository.save(any(SalesOrder.class))).thenAnswer(inv -> inv.getArgument(0));

        SalesOrder result = commandService.handle(new RemoveProductFromOrderCommand(ORDER_ID, item.getId()));

        assertTrue(result.getItems().isEmpty());
    }

    @Test
    void removeProductFromOrder_orderNotFound_throwsException() {
        when(salesOrderRepository.findById(ORDER_ID)).thenReturn(Optional.empty());

        assertThrows(SalesOrderNotFoundException.class,
                () -> commandService.handle(new RemoveProductFromOrderCommand(ORDER_ID, "any-item")));
    }

    @Test
    void cancelOrder_pendingOrder_setsStatusToCancelled() {
        SalesOrder order = pendingOrderWith(supplyItem("supply-1", 1));
        when(salesOrderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));
        when(salesOrderRepository.save(any(SalesOrder.class))).thenAnswer(inv -> inv.getArgument(0));

        commandService.handle(new CancelSalesOrderCommand(ORDER_ID));

        assertEquals(SalesOrderStatus.CANCELLED, order.getStatus());
        verify(salesOrderRepository, times(1)).save(order);
    }

    @Test
    void cancelOrder_orderNotFound_throwsException() {
        when(salesOrderRepository.findById(ORDER_ID)).thenReturn(Optional.empty());

        assertThrows(SalesOrderNotFoundException.class,
                () -> commandService.handle(new CancelSalesOrderCommand(ORDER_ID)));
    }
}
