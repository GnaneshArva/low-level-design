package com.notification.channel;

/**
 * Thrown by a NotificationChannel when delivery fails.
 * Allows the retry handler to distinguish channel failure from programming errors.
 */
public class ChannelDeliveryException extends RuntimeException {

    public ChannelDeliveryException(String channel, String reason) {
        super("[" + channel + "] Delivery failed: " + reason);
    }

    public ChannelDeliveryException(String channel, String reason, Throwable cause) {
        super("[" + channel + "] Delivery failed: " + reason, cause);
    }
}
