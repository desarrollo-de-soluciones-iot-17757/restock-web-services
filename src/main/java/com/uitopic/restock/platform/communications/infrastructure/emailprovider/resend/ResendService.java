package com.uitopic.restock.platform.communications.infrastructure.emailprovider.resend;

import com.uitopic.restock.platform.communications.application.internal.outboundservices.emailprovider.EmailService;

/**
 * ResendService is an interface that extends EmailService and is responsible for sending emails using the Resend email provider.
 * It inherits all the methods defined in EmailService and can be implemented to provide the actual logic for sending emails through Resend.
 */
public interface ResendService extends EmailService {
}
