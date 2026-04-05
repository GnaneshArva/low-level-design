package com.notification.retry;

import com.notification.channel.NotificationChannel;
import com.notification.model.Notification;

/**
 * Chain of Responsibility: each handler attempts delivery and decides
 * whether to retry, pass to the next handler, or give up.
 *
 * ISP: Focused solely on retry orchestration.
 * DIP: NotificationService depends on this interface, not the concrete handler.
 *
 * The chain pattern allows composing behaviors:
 *   RetryHandler → FallbackHandler → DeadLetterHandler
 * Each handler calls next.handle() to continue the chain.
 */
public interface RetryHandler {

    /**
     * Attempt delivery via the given channel. On failure, apply retry policy
     * before delegating to the next handler in the chain (if any).
     */
    void handle(Notification notification, NotificationChannel channel);

    /**
     * Links the next handler in the chain (e.g., fallback handler).
     */
    RetryHandler setNext(RetryHandler next);
}
