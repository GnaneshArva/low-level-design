package com.lld.filestorage.storage;

import com.lld.filestorage.exception.StorageException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-process storage — fast, volatile, ideal for testing and demos.
 *
 * Trade-off: data is lost on JVM restart; no persistence guarantee.
 * ConcurrentHashMap gives thread-safe read/write without external locking.
 */
public final class InMemoryStorageStrategy implements StorageStrategy {

    // storageKey → content
    private final Map<String, byte[]> store = new ConcurrentHashMap<>();

    @Override
    public void store(final String storageKey, final byte[] content) {
        validateKey(storageKey);
        if (content == null) throw new IllegalArgumentException("content must not be null");
        store.put(storageKey, content.clone());   // defensive copy
    }

    @Override
    public byte[] retrieve(final String storageKey) {
        validateKey(storageKey);
        final byte[] data = store.get(storageKey);
        if (data == null) {
            throw new StorageException("No data found for key: " + storageKey);
        }
        return data.clone();  // defensive copy
    }

    @Override
    public void delete(final String storageKey) {
        validateKey(storageKey);
        store.remove(storageKey);   // idempotent
    }

    private void validateKey(final String key) {
        if (key == null || key.isBlank()) throw new IllegalArgumentException("storageKey required");
    }

    /** Visible for testing / diagnostics. */
    public int storedEntryCount() { return store.size(); }
}
