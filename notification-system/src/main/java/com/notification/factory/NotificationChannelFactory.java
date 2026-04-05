package com.notification.factory;

import com.notification.channel.EmailChannel;
import com.notification.channel.NotificationChannel;
import com.notification.channel.PushChannel;
import com.notification.channel.SmsChannel;
import com.notification.exception.UnsupportedChannelException;
import com.notification.model.ChannelType;

import java.util.EnumMap;
import java.util.Map;

/**
 * Factory for resolving NotificationChannel implementations by ChannelType.
 *
 * Design decisions:
 * - Registry-based factory (Map) preferred over switch/if-else chains.
 *   Rationale: switch chains require modification for every new channel (violates OCP).
 *   Registry lookup is O(1) and extensible without modifying this class.
 *
 * - Channels are singletons within the factory (stateless, safe to reuse).
 *   If channels needed per-request state, we'd use a Supplier<NotificationChannel>.
 *
 * - DIP: callers depend on NotificationChannel abstraction returned here,
 *   never on EmailChannel or SmsChannel directly.
 *
 * Extensibility: To add WhatsAppChannel, register it in the constructor only.
 */
public class NotificationChannelFactory {

    private final Map<ChannelType, NotificationChannel> registry;

    public NotificationChannelFactory() {
        registry = new EnumMap<>(ChannelType.class);

        // Registration is the ONLY place touched when adding a new channel
        registry.put(ChannelType.EMAIL, new EmailChannel());
        registry.put(ChannelType.SMS,   new SmsChannel());
        registry.put(ChannelType.PUSH,  new PushChannel());
    }

    /**
     * Returns the channel implementation for the given type.
     *
     * @throws UnsupportedChannelException if no implementation is registered
     */
    public NotificationChannel getChannel(ChannelType type) {
        NotificationChannel channel = registry.get(type);
        if (channel == null) {
            throw new UnsupportedChannelException(type.name());
        }
        return channel;
    }
}
