
package com.cache.config;

import com.cache.eviction.EvictionPolicy;

public class CacheConfig<K> {

    private final int capacity;
    private final long ttlMillis;
    private final EvictionPolicy<K> evictionPolicy;

    public CacheConfig(int capacity, long ttlMillis, EvictionPolicy<K> evictionPolicy) {
        this.capacity = capacity;
        this.ttlMillis = ttlMillis;
        this.evictionPolicy = evictionPolicy;
    }

    public int getCapacity() { return capacity; }

    public long getTtlMillis() { return ttlMillis; }

    public EvictionPolicy<K> getEvictionPolicy() { return evictionPolicy; }
}
