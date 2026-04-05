package com.notification.facade;

import com.notification.factory.NotificationChannelFactory;
import com.notification.model.*;
import com.notification.retry.*;
import com.notification.service.NotificationService;
import com.notification.template.SimpleTemplateEngine;
import com.notification.template.TemplateEngine;

import java.util.Map;

/**
 * Facade: hides wiring complexity behind a clean, client-friendly API.
 *
 * Clients interact with this class only — they never instantiate channels,
 * factories, retry handlers, or the service directly.
 *
 * Design decisions:
 * - Default wiring uses exponential backoff with 3 retries + SMS fallback chain.
 * - Overloaded sendXxx() methods cover the most common use cases.
 * - Advanced users can call send(Notification) with a fully configured object.
 *
 * Trade-off: The Facade owns construction wiring. In a Spring application,
 * this would be replaced by @Configuration + @Bean methods — the Facade
 * is the pure-Java equivalent of a DI container configuration class.
 */
public class NotificationFacade {

    private final NotificationService notificationService;
    private final SimpleTemplateEngine templateEngine; // Concrete type exposed only here for template registration

    /**
     * Creates a fully wired facade with default retry + fallback chain.
     */
    public NotificationFacade() {
        NotificationChannelFactory channelFactory = new NotificationChannelFactory();
        SimpleTemplateEngine engine = new SimpleTemplateEngine();

        // Build Chain of Responsibility: Retry → Fallback
        FallbackHandler fallbackHandler = new FallbackHandler(channelFactory);
        RetryNotificationHandler retryHandler = new RetryNotificationHandler(
                RetryPolicy.builder()
                        .maxRetries(3)
                        .initialBackoffMs(100) // Short for demo; use 500-2000ms in production
                        .backoffStrategy(new ExponentialBackoffStrategy())
                        .build()
        );
        retryHandler.setNext(fallbackHandler); // Chain: retry first, then fallback

        this.notificationService = new NotificationService(channelFactory, engine, retryHandler);
        this.templateEngine = engine;
    }

    /**
     * Advanced constructor: allows injecting custom wiring (for testing or custom policy).
     */
    public NotificationFacade(NotificationService notificationService,
                               SimpleTemplateEngine templateEngine) {
        this.notificationService = notificationService;
        this.templateEngine = templateEngine;
    }

    // ── Simplified send methods (covers 80% of use cases) ────────────────────

    /**
     * Sends an OTP notification via the specified channel.
     */
    public void sendOtp(Recipient recipient, String otp, ChannelType channel) {
        Notification notification = Notification.builder()
                .recipient(recipient)
                .subject("Your OTP")
                .templateKey("OTP")
                .templateData(Map.of("name", recipient.getName(), "otp", otp))
                .priority(Priority.CRITICAL)
                .channel(channel)
                .fallback(channel == ChannelType.EMAIL ? ChannelType.SMS : ChannelType.EMAIL)
                .build();

        notificationService.send(notification);
    }

    /**
     * Sends an order confirmation via email with SMS fallback.
     */
    public void sendOrderConfirmation(Recipient recipient, String orderId, String deliveryDate) {
        Notification notification = Notification.builder()
                .recipient(recipient)
                .subject("Order Confirmed: #" + orderId)
                .templateKey("ORDER_CONFIRMATION")
                .templateData(Map.of(
                        "name", recipient.getName(),
                        "orderId", orderId,
                        "deliveryDate", deliveryDate
                ))
                .priority(Priority.HIGH)
                .channel(ChannelType.EMAIL)
                .fallback(ChannelType.SMS)
                .build();

        notificationService.send(notification);
    }

    /**
     * Sends a promotional push notification (low priority, no fallback needed).
     */
    public void sendPromo(Recipient recipient, String discount, String code) {
        Notification notification = Notification.builder()
                .recipient(recipient)
                .subject("Exclusive Offer Just for You!")
                .templateKey("PROMO")
                .templateData(Map.of(
                        "name", recipient.getName(),
                        "discount", discount,
                        "code", code
                ))
                .priority(Priority.LOW)
                .channel(ChannelType.PUSH)
                .build();

        notificationService.send(notification);
    }

    /**
     * Full-control method: send any pre-built notification directly.
     */
    public void send(Notification notification) {
        notificationService.send(notification);
    }

    /**
     * Batch send respecting priority order across the batch.
     */
    public void sendAll(Iterable<Notification> notifications) {
        notificationService.sendAll(notifications);
    }

    /**
     * Allows runtime registration of custom templates.
     */
    public void registerTemplate(String key, String template) {
        templateEngine.registerTemplate(key, template);
    }

    /**
     * Query delivery status for a sent notification.
     */
    public DeliveryStatus getStatus(String notificationId) {
        return notificationService.getDeliveryStatus(notificationId);
    }
}
