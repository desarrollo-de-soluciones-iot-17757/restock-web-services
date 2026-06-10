package com.uitopic.restock.platform.shared.domain.model.valueobjects;

/**
 * Value object representing a batch ID used for tracking inventory batches.
 *
 * @param batchId unique identifier of the batch, provided by the request
 */
public record BatchId(
        String batchId
) {

    /**
     * Creates a batch ID value object.
     *
     * @param batchId unique identifier of the batch, provided by the request
     */
    public BatchId {
        if (batchId == null || batchId.isBlank()) {
            throw new IllegalArgumentException("Batch ID cannot be null or blank");
        }
    }

    /**
     * Returns the unique identifier of the batch.
     *
     * @return the batch ID
     */
    public String getBatchId() {
        return batchId;
    }
}
