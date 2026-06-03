package com.uitopic.restock.platform.resources.interfaces.rest.transform;

import com.uitopic.restock.platform.resources.domain.model.commands.TransferBatchStockCommand;
import com.uitopic.restock.platform.resources.domain.model.valueobjects.Stock;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.TransferBatchStockResource;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.UnitMeasurement;

/**
 * Assembler to convert TransferBatchStockResource into TransferBatchStockCommand.
 */
public class TransferBatchStockCommandFromResourceAssembler {

    /**
     * Converts a transfer batch stock request into a command.
     *
     * @param sourceBatchId source batch identifier
     * @param resource transfer request resource
     * @return transfer batch stock command
     */
    public static TransferBatchStockCommand toCommandFromResource(
            String sourceBatchId,
            TransferBatchStockResource resource
    ) {
        return new TransferBatchStockCommand(
                sourceBatchId,
                resource.targetBranchId(),
                new Stock(resource.quantity(), new UnitMeasurement(resource.unitMeasurement())),
                resource.reason()
        );
    }
}