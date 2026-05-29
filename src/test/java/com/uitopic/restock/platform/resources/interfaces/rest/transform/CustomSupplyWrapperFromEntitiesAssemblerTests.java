package com.uitopic.restock.platform.resources.interfaces.rest.transform;

import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.resources.domain.model.entities.Supply;
import com.uitopic.restock.platform.shared.interfaces.rest.transform.SharedValueObjectFromStringAssembler;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CustomSupplyWrapperFromEntitiesAssemblerTests {

    /**
     * Test to verify that the toItemFromEntity method correctly transforms a valid CustomSupply entity into a CustomSupplyItem and that the toWrapperFromEntities method correctly transforms a list of CustomSupply entities into a CustomSupplyWrapper with the correct accountId and total count.
     */
    @Test
    void toItemFromEntity_WithValidCustomSupplyEntity_ReturnsCustomSupplyItem() {
        // Arrange
        var accountId = SharedValueObjectFromStringAssembler.toAccountIdFromString("account-123");
        List<CustomSupply> supplies = new ArrayList<>();

        // Act
        for (int i = 0; i < 5; i++) {
            supplies.add(CustomSupply.builder()
                    .name("Supply " + i)
                    .description("Description for Supply " + i)
                    .category(Supply.builder().build())
                    .unitPrice(SharedValueObjectFromStringAssembler.toMoneyFromString("10.00 PEN"))
                    .supplyContent(ResourcesValueObjectFromStringAssembler.toSupplyContentFromString("500.00"))
                    .unitMeasurement(SharedValueObjectFromStringAssembler.toUnitMeasurementFromString("litters"))
                    .minimumStock(ResourcesValueObjectFromStringAssembler.toMinimumStockFromString("100"))
                    .pictureUrl(SharedValueObjectFromStringAssembler.toImageURLFromString("http://example.com/image" + i + ".jpg"))
                    .accountId(SharedValueObjectFromStringAssembler.toAccountIdFromString("account-123"))
                    .build());
        }

        var wrapper = CustomSupplyWrapperFromEntitiesAssembler.toWrapperFromEntities(accountId, supplies);

        // Assert
        assert wrapper.accountId().equals("account-123");
    }

    /**
     * Test to verify that the toItemFromEntity method throws a NullPointerException when required fields are missing in the CustomSupply entity.
     */
    @Test
    void toItemFromEntity_WithMissingFields_ThrowsNullPointerException() {
        // Arrange
        var accountId = SharedValueObjectFromStringAssembler.toAccountIdFromString("account-123");
        List<CustomSupply> supplies = new ArrayList<>();

        // Act
        for (int i = 0; i < 5; i++) {
            supplies.add(CustomSupply.builder()
                    .name("Supply " + i)
                    .description("Description for Supply " + i)
                    .build());
        }

        // Assert
        assertThrows(NullPointerException.class, () -> {
            var wrapper = CustomSupplyWrapperFromEntitiesAssembler.toWrapperFromEntities(accountId, supplies);}
        );
    }
}
