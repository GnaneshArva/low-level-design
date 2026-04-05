package com.notification.exception;

/**
 * Thrown when a notification cannot be delivered through any channel.
 */
public class NotificationDeliveryException extends RuntimeException {

    private final String notificationId;

    public NotificationDeliveryException(String notificationId, String message) {
        super(message);
        this.notificationId = notificationId;
    }

    public NotificationDeliveryException(String notificationId, String message, Throwable cause) {
        super(message, cause);
        this.notificationId = notificationId;
    }

    public String getNotificationId() { return notificationId; }
}
