package com.uitopic.restock.platform.tracking.domain.exceptions;

/**
 * Custom exception thrown when telemetry values are invalid or missing.
 */
public class TelemetryValuesException extends RuntimeException {
    public TelemetryValuesException(String message) {
        super(message);
    }
}
