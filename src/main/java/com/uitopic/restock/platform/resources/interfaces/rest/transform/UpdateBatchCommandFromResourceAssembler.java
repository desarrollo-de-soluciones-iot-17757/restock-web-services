package com.uitopic.restock.platform.resources.interfaces.rest.transform;

import com.uitopic.restock.platform.resources.domain.model.commands.UpdateBatchCommand;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.UpdateBatchResource;

import java.time.LocalDate;

/**
 * Assembler to convert UpdateBatchResource into UpdateBatchCommand.
 */
public class UpdateBatchCommandFromResourceAssembler {

    /**
     * Converts an update batch request into a command.
     *
     * @param batchId batch identifier
     * @param resource update batch request resource
     * @return update batch command
     */
    public static UpdateBatchCommand toCommandFromResource(String batchId, UpdateBatchResource resource) {
        return new UpdateBatchCommand(
                batchId,
                resource.code(),
                resource.currentStock(),
                parseDate(resource.expirationDate())
        );
    }

    private static LocalDate parseDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return LocalDate.parse(value);
    }
}