package com.notification.channel;

import com.notification.model.Notification;

/**
 * Strategy interface for notification delivery.
 *
 * Design decisions:
 * - ISP: This interface is deliberately thin — one method, one responsibility.
 * - OCP: New channels (WhatsApp, Slack) implement this without touching existing code.
 * - DIP: Higher layers depend on this abstraction, not concrete channel classes.
 *
 * LSP guarantee: every implementation MUST attempt delivery and throw
 * ChannelDeliveryException on failure — never return silently on error.
 */
public interface NotificationChannel {

    /**
     * Attempts to deliver the notification.
     *
     * @param notification the notification to deliver (renderedMessage already applied)
     * @throws ChannelDeliveryException if delivery fails for any reason
     */
    void send(Notification notification);
}
