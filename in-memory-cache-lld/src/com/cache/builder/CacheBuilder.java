
package com.cache.builder;

import com.cache.config.CacheConfig;
import com.cache.eviction.EvictionPolicy;

public class CacheBuilder<K> {

    private int capacity;
    private long ttlMillis;
    private EvictionPolicy<K> evictionPolicy;

    public CacheBuilder<K> capacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    public CacheBuilder<K> ttl(long ttlMillis) {
        this.ttlMillis = ttlMillis;
        return this;
    }

    public CacheBuilder<K> evictionPolicy(EvictionPolicy<K> evictionPolicy) {
        this.evictionPolicy = evictionPolicy;
        return this;
    }

    public CacheConfig<K> build() {
        return new CacheConfig<>(capacity, ttlMillis, evictionPolicy);
    }
}
