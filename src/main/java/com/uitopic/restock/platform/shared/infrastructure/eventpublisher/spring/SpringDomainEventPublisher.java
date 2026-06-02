package com.uitopic.restock.platform.shared.infrastructure.eventpublisher.spring;

import com.uitopic.restock.platform.shared.application.internal.eventhandlers.DomainEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * The SpringDomainEventPublisher class is an implementation of the DomainEventPublisher interface that utilizes the Spring framework's ApplicationEventPublisher to publish domain events. This class serves as a bridge between the domain layer and the Spring event publishing mechanism, allowing domain events to be broadcasted to interested subscribers within the application context. By leveraging Spring's event handling capabilities, this implementation ensures that domain events are efficiently managed and delivered to the appropriate components that have registered to receive notifications about these events.
 */
@Component
public class SpringDomainEventPublisher implements DomainEventPublisher {

    // The ApplicationEventPublisher is a Spring framework component that provides the functionality to publish events within the application context. It allows components to communicate with each other by broadcasting events and enabling subscribers to react to those events. By using this publisher, the SpringDomainEventPublisher can leverage Spring's event handling mechanism to efficiently manage domain events and ensure that they are delivered to the appropriate subscribers.
    private final ApplicationEventPublisher publisher;

    // Constructor that initializes the SpringDomainEventPublisher with an instance of ApplicationEventPublisher. This allows the publisher to use Spring's event publishing mechanism to broadcast domain events to interested subscribers.
    public SpringDomainEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    /**
     * Publishes a domain event to all interested subscribers. This method is responsible for broadcasting the event to any components that have registered to receive notifications about this type of event. The implementation should ensure that the event is delivered in a timely manner and that all relevant subscribers are notified.
     *
     * @param event the domain event to be published. This can be any object that represents a significant occurrence in the domain, such as an entity being created, updated, or deleted. The event should contain all relevant information about the occurrence to allow subscribers to react appropriately.
     */
    @Override
    public void publish(Object event) {
        publisher.publishEvent(event);
    }
}
