package com.uitopic.restock.platform.communications.domain.model.valueobjects;

/**
 * Enumeration representing the different types of alerts that can be sent within the communications context. This enum is used to categorize alerts and determine the appropriate handling and email templates for each type of alert.
 */
public enum AlertType {
    INVENTORY,
    DISCREPANCY,
    DEVICE
}
