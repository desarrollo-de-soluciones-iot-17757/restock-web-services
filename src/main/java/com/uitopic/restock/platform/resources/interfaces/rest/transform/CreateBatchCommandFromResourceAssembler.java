package com.uitopic.restock.platform.resources.interfaces.rest.transform;

import com.uitopic.restock.platform.resources.domain.model.commands.CreateBatchCommand;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.CreateBatchResource;

import java.time.LocalDate;

/**
 * Assembler to convert CreateBatchResource into CreateBatchCommand.
 */
public class CreateBatchCommandFromResourceAssembler {

    /**
     * Converts a create batch request into a command.
     *
     * @param accountId account identifier received as query parameter
     * @param resource create batch request resource
     * @return create batch command
     */
    public static CreateBatchCommand toCommandFromResource(
            String accountId,
            CreateBatchResource resource
    ) {
        return new CreateBatchCommand(
                resource.code(),
                resource.currentStock(),
                resource.customSupplyId(),
                resource.branchId(),
                accountId,
                parseDate(resource.expirationDate()),
                LocalDate.now()
        );
    }

    private static LocalDate parseDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return LocalDate.parse(value);
    }
}