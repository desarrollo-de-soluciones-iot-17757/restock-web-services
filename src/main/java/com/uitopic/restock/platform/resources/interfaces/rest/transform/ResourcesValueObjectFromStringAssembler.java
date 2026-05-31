package com.uitopic.restock.platform.resources.interfaces.rest.transform;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.MinimumStock;
import com.uitopic.restock.platform.resources.domain.model.valueobjects.SupplyContent;
import jakarta.validation.constraints.NotEmpty;

/**
 * Assembler to convert string representations of resources value objects (e.g. MinimumStock, SupplyContent)
 * into their corresponding domain instances within the resources bounded context.
 */
public class ResourcesValueObjectFromStringAssembler {

    /**
     * Converts a string representation of minimum stock into a MinimumStock value object. The string should represent a numeric value (e.g., "10" for 10 units). If the string cannot be parsed into a valid integer, an IllegalArgumentException is thrown.
     *
     * @param minimumStockStr the string representation of the minimum stock to be converted
     * @return a MinimumStock value object representing the parsed minimum stock
     */
    public static MinimumStock toMinimumStockFromString(@NotEmpty String minimumStockStr) {
        try {
            int minimumStockValue = Integer.parseInt(minimumStockStr);
            return new MinimumStock(minimumStockValue);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid minimum stock value: " + minimumStockStr, e);
        }
    }

    /**
     * Converts a string representation of supply content into a SupplyContent value object. The string should represent a numeric value (e.g., "500" for 500ml, "1" for 1kg, etc.). If the string cannot be parsed into a valid double, an IllegalArgumentException is thrown.
     *
     * @param supplyContentStr the string representation of the supply content to be converted
     * @return a SupplyContent value object representing the parsed supply content
     */
    public static SupplyContent toSupplyContentFromString(@NotEmpty String supplyContentStr) {
        try {
            return new SupplyContent(Double.valueOf(supplyContentStr));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid supply content value: " + supplyContentStr, e);
        }
    }
}
