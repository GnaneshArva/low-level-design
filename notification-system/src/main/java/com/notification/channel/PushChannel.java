package com.notification.channel;

import com.notification.model.Notification;

/**
 * Push notification channel via a hypothetical FCM/APNs wrapper.
 *
 * Design note: Device tokens can become invalid (user uninstalled app).
 * In production, a 410 response from FCM would trigger token cleanup.
 */
public class PushChannel implements NotificationChannel {

    @Override
    public void send(Notification notification) {
        String token = notification.getRecipient().getDeviceToken();

        if (token == null || token.isBlank()) {
            throw new ChannelDeliveryException("PUSH",
                    "Recipient has no device token: " + notification.getRecipient().getName());
        }

        System.out.printf("[PUSH] Sending to device: %s | Title: '%s' | Body: %s%n",
                token, notification.getSubject(), notification.getRenderedMessage());

        System.out.printf("[PUSH] ✓ Delivered to device %s%n", token);
    }
}
