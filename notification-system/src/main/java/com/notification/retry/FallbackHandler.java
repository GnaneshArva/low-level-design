package com.notification.retry;

import com.notification.channel.ChannelDeliveryException;
import com.notification.channel.NotificationChannel;
import com.notification.exception.NotificationDeliveryException;
import com.notification.factory.NotificationChannelFactory;
import com.notification.model.DeliveryStatus;
import com.notification.model.Notification;

/**
 * Fallback handler: activated after all retries on the primary channel are exhausted.
 *
 * Attempts a single delivery via the notification's configured fallback channel.
 * If the notification has no fallback, or fallback also fails, marks as EXHAUSTED.
 *
 * Chain position: RetryNotificationHandler → FallbackHandler → (end of chain)
 *
 * Design note: FallbackHandler receives the same NotificationChannel argument from
 * the chain contract but resolves its own channel from the factory using the
 * notification's fallbackChannel field. This keeps the interface consistent
 * while allowing the handler to override the delivery target.
 */
public class FallbackHandler implements RetryHandler {

    private final NotificationChannelFactory channelFactory;
    private RetryHandler next;

    public FallbackHandler(NotificationChannelFactory channelFactory) {
        this.channelFactory = channelFactory;
    }

    @Override
    public RetryHandler setNext(RetryHandler next) {
        this.next = next;
        return next;
    }

    @Override
    public void handle(Notification notification, NotificationChannel ignoredChannel) {
        if (!notification.hasFallback()) {
            notification.setStatus(DeliveryStatus.EXHAUSTED);
            throw new NotificationDeliveryException(notification.getId(),
                    "Primary channel failed, no fallback configured.");
        }

        System.out.printf("[FALLBACK] Primary channel failed. Attempting fallback via %s%n",
                notification.getFallbackChannel());

        NotificationChannel fallbackChannel = channelFactory.getChannel(notification.getFallbackChannel());

        try {
            notification.incrementAttempt();
            fallbackChannel.send(notification);
            notification.setStatus(DeliveryStatus.FALLBACK_DELIVERED);
            System.out.printf("[FALLBACK] ✓ Delivered via fallback channel %s%n",
                    notification.getFallbackChannel());

        } catch (ChannelDeliveryException ex) {
            System.out.printf("[FALLBACK] Fallback channel %s also failed: %s%n",
                    notification.getFallbackChannel(), ex.getMessage());

            if (next != null) {
                next.handle(notification, fallbackChannel);
            } else {
                notification.setStatus(DeliveryStatus.EXHAUSTED);
                throw new NotificationDeliveryException(notification.getId(),
                        "All channels exhausted including fallback: " + notification.getFallbackChannel(), ex);
            }
        }
    }
}
