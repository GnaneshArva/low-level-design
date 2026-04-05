package com.notification.service;

import com.notification.channel.NotificationChannel;
import com.notification.exception.NotificationDeliveryException;
import com.notification.factory.NotificationChannelFactory;
import com.notification.model.DeliveryStatus;
import com.notification.model.Notification;
import com.notification.model.Priority;
import com.notification.retry.RetryHandler;
import com.notification.template.TemplateEngine;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Core notification orchestration service.
 *
 * Responsibilities (SRP):
 *   1. Validate and enqueue notifications
 *   2. Apply templates before delivery
 *   3. Delegate delivery to the appropriate channel via RetryHandler chain
 *   4. Track delivery status
 *
 * NOT responsible for:
 *   - Channel delivery mechanics (→ NotificationChannel)
 *   - Retry logic (→ RetryHandler chain)
 *   - Template rendering (→ TemplateEngine)
 *   - Channel resolution (→ NotificationChannelFactory)
 *
 * Design note on priority queue:
 *   PriorityBlockingQueue orders by Priority enum ordinal descending (CRITICAL first).
 *   In a fully async system, a consumer thread would drain this queue. Here, send()
 *   enqueues and immediately processes — demonstrating the priority mechanism
 *   without requiring a background thread for interview clarity.
 */
public class NotificationService {

    private final NotificationChannelFactory channelFactory;
    private final TemplateEngine templateEngine;
    private final RetryHandler retryHandlerChain;

    // Priority queue: CRITICAL > HIGH > MEDIUM > LOW
    private final PriorityBlockingQueue<Notification> notificationQueue;

    // Delivery status registry: notificationId → DeliveryStatus
    private final Map<String, DeliveryStatus> deliveryStatusMap;

    public NotificationService(NotificationChannelFactory channelFactory,
                               TemplateEngine templateEngine,
                               RetryHandler retryHandlerChain) {
        if (channelFactory == null)    throw new IllegalArgumentException("ChannelFactory required");
        if (templateEngine == null)    throw new IllegalArgumentException("TemplateEngine required");
        if (retryHandlerChain == null) throw new IllegalArgumentException("RetryHandlerChain required");

        this.channelFactory    = channelFactory;
        this.templateEngine    = templateEngine;
        this.retryHandlerChain = retryHandlerChain;

        // Higher priority ordinal = processed first
        this.notificationQueue = new PriorityBlockingQueue<>(
                16,
                Comparator.comparing(Notification::getPriority).reversed()
        );

        this.deliveryStatusMap = new ConcurrentHashMap<>();
    }

    /**
     * Enqueues a notification for delivery and immediately processes it.
     *
     * In a production async system, enqueue() and processNext() would be separate.
     * Combined here for interview clarity.
     */
    public void send(Notification notification) {
        validateNotification(notification);

        System.out.printf("%n=== Queueing notification [%s] priority=%s channel=%s ===%n",
                notification.getId(), notification.getPriority(), notification.getPrimaryChannel());

        // Step 1: Apply template — fail fast if template is missing
        applyTemplate(notification);

        // Step 2: Enqueue with priority ordering
        notificationQueue.offer(notification);
        deliveryStatusMap.put(notification.getId(), DeliveryStatus.PENDING);

        // Step 3: Process (synchronous for interview demo; async in production)
        processNext();
    }

    /**
     * Processes the highest-priority notification from the queue.
     */
    private void processNext() {
        Notification notification = notificationQueue.poll();
        if (notification == null) return;

        NotificationChannel channel = channelFactory.getChannel(notification.getPrimaryChannel());

        try {
            retryHandlerChain.handle(notification, channel);
        } catch (NotificationDeliveryException ex) {
            System.out.printf("[SERVICE] ✗ Final failure for %s: %s%n",
                    notification.getId(), ex.getMessage());
        } finally {
            // Always record final status, regardless of success or failure
            deliveryStatusMap.put(notification.getId(), notification.getStatus());
            System.out.printf("[SERVICE] Status recorded: %s → %s%n",
                    notification.getId(), notification.getStatus());
        }
    }

    /**
     * Returns the current delivery status for a given notification ID.
     */
    public DeliveryStatus getDeliveryStatus(String notificationId) {
        return deliveryStatusMap.getOrDefault(notificationId, DeliveryStatus.PENDING);
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private void applyTemplate(Notification notification) {
        String rendered = templateEngine.render(notification);
        notification.setRenderedMessage(rendered);
        System.out.printf("[SERVICE] Template '%s' rendered: %s%n",
                notification.getTemplateKey(), rendered);
    }

    private void validateNotification(Notification notification) {
        if (notification == null) {
            throw new IllegalArgumentException("Notification must not be null");
        }
        if (notification.getPriority() == null) {
            throw new IllegalArgumentException("Priority must not be null");
        }
    }

    /**
     * Convenience: send multiple notifications respecting their individual priorities.
     * The queue ensures highest-priority items are processed first across the batch.
     */
    public void sendAll(Iterable<Notification> notifications) {
        // Enqueue all first, then process in priority order
        for (Notification n : notifications) {
            validateNotification(n);
            applyTemplate(n);
            notificationQueue.offer(n);
            deliveryStatusMap.put(n.getId(), DeliveryStatus.PENDING);
        }
        while (!notificationQueue.isEmpty()) {
            processNext();
        }
    }
}
