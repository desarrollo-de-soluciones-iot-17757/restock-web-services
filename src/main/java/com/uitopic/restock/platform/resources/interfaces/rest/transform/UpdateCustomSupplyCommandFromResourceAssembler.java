package com.uitopic.restock.platform.resources.interfaces.rest.transform;

import com.uitopic.restock.platform.resources.domain.model.commands.UpdateCustomSupplyCommand;
import com.uitopic.restock.platform.resources.domain.model.valueobjects.StockRange;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.UpdateCustomSupplyResource;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.Money;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.UnitMeasurement;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Assembler to convert UpdateCustomSupplyResource into UpdateCustomSupplyCommand.
 */
public class UpdateCustomSupplyCommandFromResourceAssembler {

    /**
     * Converts an update custom supply request into a command.
     *
     * All fields except the custom supply ID are optional.
     * The base supply ID is not editable from this endpoint.
     *
     * @param customSupplyId custom supply identifier
     * @param resource update custom supply request resource
     * @return update custom supply command
     */
    public static UpdateCustomSupplyCommand toCommandFromResource(
            String customSupplyId,
            UpdateCustomSupplyResource resource
    ) {
        return new UpdateCustomSupplyCommand(
                customSupplyId,
                resource.name(),
                resource.description(),
                buildStockRange(resource.minimumStock(), resource.maximumStock()),
                buildMoney(resource.unitPrice()),
                buildUnitMeasurement(resource.unitMeasurement()),
                getBytes(resource.image()),
                getFileName(resource.image())
        );
    }

    private static StockRange buildStockRange(Double minimumStock, Double maximumStock) {
        if (minimumStock == null && maximumStock == null) {
            return null;
        }

        if (minimumStock == null || maximumStock == null) {
            throw new IllegalArgumentException("Minimum stock and maximum stock must be sent together");
        }

        return new StockRange(minimumStock, maximumStock);
    }

    private static Money buildMoney(String unitPrice) {
        if (unitPrice == null || unitPrice.isBlank()) {
            return null;
        }

        return ResourcesValueObjectFromStringAssembler.toMoneyFromString(unitPrice);
    }

    private static UnitMeasurement buildUnitMeasurement(String unitMeasurement) {
        if (unitMeasurement == null || unitMeasurement.isBlank()) {
            return null;
        }

        return new UnitMeasurement(unitMeasurement);
    }

    private static byte[] getBytes(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read custom supply image", e);
        }
    }

    private static String getFileName(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        return file.getOriginalFilename();
    }
}