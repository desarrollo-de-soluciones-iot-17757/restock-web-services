package com.uitopic.restock.platform.shared.application.internal.eventhandlers;

/**
 * Interface for publishing domain events. Implementations of this interface are responsible for delivering domain events to interested subscribers, which may include other parts of the application
 * or external systems. The publish method takes an event object, which can be any type of domain event defined in the application. This interface abstracts away the details of how events are delivered, allowing for flexibility in the choice
 */
public interface DomainEventPublisher {

    /**
     * Publishes a domain event to all interested subscribers. The implementation of this method is responsible for delivering the event to the appropriate handlers, which may involve asynchronous processing or integration with external messaging systems.
     * @param event the domain event to be published. This can be any object that represents a significant occurrence in the domain, such as an entity being created, updated, or deleted. The event should contain all relevant information about the occurrence to allow subscribers to react appropriately.
     */
    void publish(Object event);
}
