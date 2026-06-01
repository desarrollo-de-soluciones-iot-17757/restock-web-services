package com.uitopic.restock.platform.iam.domain.model.valueobjects;

/**
 * Defines the types of roles available in the IAM system.
 * These govern the access control levels of the authenticated users.
 */
public enum RoleType {
    RESTAURANTADMIN,
    RETAILADMIN,
    ADMIN,
    CASHIER,
    WAREHOUSEMAN
}
