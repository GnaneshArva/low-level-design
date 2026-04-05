package com.notification.channel;

import com.notification.model.Notification;

/**
 * Email delivery channel.
 *
 * Design note: Simulates SMTP delivery. In production this would inject an
 * EmailClient dependency (e.g., JavaMail, SES SDK) — making it easily testable.
 * The channel only concerns itself with delivery mechanics, not retry or routing.
 */
public class EmailChannel implements NotificationChannel {

    // Simulated failure rate to demonstrate retry behavior in demos
    private static final double SIMULATED_FAILURE_RATE = 0.3;

    @Override
    public void send(Notification notification) {
        String email = notification.getRecipient().getEmail();

        if (email == null || email.isBlank()) {
            throw new ChannelDeliveryException("EMAIL",
                    "Recipient has no email address: " + notification.getRecipient().getName());
        }

        System.out.printf("[EMAIL] Sending to %s | Subject: '%s' | Body: %s%n",
                email, notification.getSubject(), notification.getRenderedMessage());

        simulateNetworkCall(notification.getId());

        System.out.printf("[EMAIL] ✓ Delivered to %s%n", email);
    }

    /**
     * Simulates occasional network-level failures to exercise retry logic.
     * In production: remove this; real failures come from the SMTP/SDK layer.
     */
    private void simulateNetworkCall(String notificationId) {
        if (Math.random() < SIMULATED_FAILURE_RATE) {
            throw new ChannelDeliveryException("EMAIL",
                    "SMTP connection timeout for notification: " + notificationId);
        }
    }
}
