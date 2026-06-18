package com.uitopic.restock.platform.communications.interfaces.rest.resources;


/**
 * Record representing a notification resource in the REST API.
 * This resource is used to transfer notification data between the server and clients.
 */
public record NotificationResource (
        String id,
        String title,
        String message,
        String severity,
        String status,
        String sourceType,
        String timestamp
){
}
