
package com.cache.core;

import com.cache.api.Cache;
import com.cache.config.CacheConfig;
import com.cache.eviction.EvictionPolicy;
import com.cache.storage.CacheStorage;

public class InMemoryCache<K, V> implements Cache<K, V> {

    private final CacheStorage<K, V> storage;
    private final EvictionPolicy<K> evictionPolicy;
    private final int capacity;
    private final long ttl;

    public InMemoryCache(CacheConfig<K> config) {
        this.capacity = config.getCapacity();
        this.ttl = config.getTtlMillis();
        this.evictionPolicy = config.getEvictionPolicy();
        this.storage = new CacheStorage<>();
    }

    @Override
    public synchronized void put(K key, V value) {
        if (storage.size() >= capacity) {
            K evictKey = evictionPolicy.evict();
            storage.remove(evictKey);
        }
        CacheEntry<K, V> entry = new CacheEntry<>(key, value, ttl);
        storage.put(key, entry);
        evictionPolicy.onPut(key);
    }

    @Override
    public synchronized V get(K key) {
        CacheEntry<K, V> entry = storage.get(key);
        if (entry == null) return null;

        if (entry.isExpired()) {
            storage.remove(key);
            return null;
        }

        entry.updateAccessTime();
        entry.incrementFrequency();
        evictionPolicy.onGet(key);
        return entry.getValue();
    }

    @Override
    public synchronized void remove(K key) {
        storage.remove(key);
    }

    @Override
    public synchronized void clear() {
        storage.clear();
    }

    @Override
    public synchronized int size() {
        return storage.size();
    }
}
