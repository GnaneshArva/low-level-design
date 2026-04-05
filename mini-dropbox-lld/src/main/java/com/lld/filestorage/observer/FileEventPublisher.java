package com.lld.filestorage.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Observer Pattern — subject / publisher.
 *
 * SRP: Manages listener registration and fan-out only.
 * Injected into the service layer so listeners are decoupled from
 * the core storage logic (DIP).
 *
 * Trade-off: synchronous fan-out for simplicity.
 * In production: use an async executor or event bus (Kafka, EventBridge).
 */
public final class FileEventPublisher {

    private final List<FileEventListener> listeners = new ArrayList<>();

    public void subscribe(final FileEventListener listener) {
        if (listener == null) throw new IllegalArgumentException("listener must not be null");
        listeners.add(listener);
    }

    public void unsubscribe(final FileEventListener listener) {
        listeners.remove(listener);
    }

    public void publish(final FileEvent event) {
        if (event == null) throw new IllegalArgumentException("event must not be null");
        for (FileEventListener listener : listeners) {
            try {
                listener.onEvent(event);
            } catch (Exception e) {
                // One failing listener must not block others — log and continue.
                System.err.println("[EventPublisher] Listener error: " + e.getMessage());
            }
        }
    }
}
