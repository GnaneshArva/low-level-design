package com.notification.channel;

import com.notification.model.Notification;

/**
 * SMS delivery channel via a hypothetical gateway (e.g., Twilio, AWS SNS).
 *
 * Design note: SMS messages are typically length-constrained (160 chars for single
 * segment). In production, this class would enforce that constraint and split
 * long messages. Kept simple here to stay interview-readable.
 */
public class SmsChannel implements NotificationChannel {

    private static final int MAX_SMS_LENGTH = 160;

    @Override
    public void send(Notification notification) {
        String phone = notification.getRecipient().getPhoneNumber();

        if (phone == null || phone.isBlank()) {
            throw new ChannelDeliveryException("SMS",
                    "Recipient has no phone number: " + notification.getRecipient().getName());
        }

        String message = notification.getRenderedMessage();
        if (message != null && message.length() > MAX_SMS_LENGTH) {
            System.out.printf("[SMS]  ⚠ Message exceeds %d chars, will be split%n", MAX_SMS_LENGTH);
        }

        System.out.printf("[SMS]  Sending to %s | Body: %s%n", phone, message);

        // No simulated failure here — SMS is the reliable fallback in our demo
        System.out.printf("[SMS]  ✓ Delivered to %s%n", phone);
    }
}
