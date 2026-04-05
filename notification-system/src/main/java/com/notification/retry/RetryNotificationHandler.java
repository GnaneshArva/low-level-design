package com.notification.retry;

import com.notification.channel.ChannelDeliveryException;
import com.notification.channel.NotificationChannel;
import com.notification.exception.NotificationDeliveryException;
import com.notification.model.DeliveryStatus;
import com.notification.model.Notification;

/**
 * Concrete retry handler: attempts delivery up to maxRetries times,
 * applying backoff between each attempt.
 *
 * If all retries are exhausted:
 *   - Delegates to the next handler in chain (e.g., FallbackHandler) if present
 *   - Otherwise marks the notification EXHAUSTED and throws
 *
 * Design note: Thread.sleep is used here for simplicity. In production, retries
 * would be scheduled asynchronously (e.g., via a delay queue or Kafka retry topic)
 * to avoid blocking threads. Synchronous retry is acceptable for interview context.
 */
public class RetryNotificationHandler implements RetryHandler {

    private final RetryPolicy retryPolicy;
    private RetryHandler next;  // Next handler in chain (nullable)

    public RetryNotificationHandler(RetryPolicy retryPolicy) {
        if (retryPolicy == null) throw new IllegalArgumentException("RetryPolicy is required");
        this.retryPolicy = retryPolicy;
    }

    @Override
    public RetryHandler setNext(RetryHandler next) {
        this.next = next;
        return next; // Fluent chaining: handler1.setNext(handler2).setNext(handler3)
    }

    @Override
    public void handle(Notification notification, NotificationChannel channel) {
        notification.setStatus(DeliveryStatus.IN_PROGRESS);

        int maxAttempts = retryPolicy.getMaxRetries() + 1; // +1 for the initial attempt
        ChannelDeliveryException lastException = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                notification.incrementAttempt();
                channel.send(notification);
                notification.setStatus(DeliveryStatus.DELIVERED);
                System.out.printf("[RETRY] ✓ Delivered on attempt %d of %d%n", attempt, maxAttempts);
                return; // Success — exit handler

            } catch (ChannelDeliveryException ex) {
                lastException = ex;
                System.out.printf("[RETRY] Attempt %d/%d failed: %s%n", attempt, maxAttempts, ex.getMessage());

                if (attempt < maxAttempts) {
                    sleep(retryPolicy.getWaitTime(attempt));
                }
            }
        }

        // All attempts exhausted — delegate to next handler or fail
        System.out.printf("[RETRY] All %d attempts exhausted for notification %s%n",
                maxAttempts, notification.getId());

        if (next != null) {
            next.handle(notification, channel);
        } else {
            notification.setStatus(DeliveryStatus.EXHAUSTED);
            throw new NotificationDeliveryException(notification.getId(),
                    "Delivery failed after " + maxAttempts + " attempts", lastException);
        }
    }

    private void sleep(long millis) {
        if (millis <= 0) return;
        try {
            System.out.printf("[RETRY] Waiting %dms before next attempt...%n", millis);
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
