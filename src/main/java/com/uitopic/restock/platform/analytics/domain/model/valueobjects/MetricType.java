package com.uitopic.restock.platform.analytics.domain.model.valueobjects;

/**
 * Enum representing the specific types of metrics tracked within each category.
 * Each value corresponds to a measurable KPI in the analytics system.
 */
public enum MetricType {
    SUPPLIES_CREATED,
    LOW_STOCK_SUPPLIES,
    ZERO_STOCK_SUPPLIES,
    DEVICES_ACTIVE,
    NOTIFICATIONS_RECEIVED,
    SALES_MADE,
    RECIPES_PROFIT,
    KITS_PROFIT
}
