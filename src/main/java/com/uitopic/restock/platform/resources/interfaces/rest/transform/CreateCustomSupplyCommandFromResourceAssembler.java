package com.uitopic.restock.platform.resources.interfaces.rest.transform;

import com.uitopic.restock.platform.resources.domain.model.commands.CreateCustomSupplyCommand;
import com.uitopic.restock.platform.resources.domain.model.valueobjects.StockRange;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.CreateCustomSupplyResource;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.UnitMeasurement;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Assembler to convert CreateCustomSupplyResource into CreateCustomSupplyCommand.
 */
public class CreateCustomSupplyCommandFromResourceAssembler {

    /**
     * Converts a create custom supply request into a command.
     *
     * The account ID is received as a query parameter and the image is received
     * as multipart/form-data.
     *
     * @param accountId account identifier received as query parameter
     * @param resource create custom supply request resource
     * @return create custom supply command
     */
    public static CreateCustomSupplyCommand toCommandFromResource(
            String accountId,
            CreateCustomSupplyResource resource
    ) {
        return new CreateCustomSupplyCommand(
                accountId,
                resource.name(),
                new StockRange(resource.minimumStock(), resource.maximumStock()),
                resource.supplyId(),
                ResourcesValueObjectFromStringAssembler.toMoneyFromString(resource.unitPrice()),
                resource.description(),
                new UnitMeasurement(resource.unitMeasurement()),
                getBytes(resource.image()),
                getFileName(resource.image())
        );
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