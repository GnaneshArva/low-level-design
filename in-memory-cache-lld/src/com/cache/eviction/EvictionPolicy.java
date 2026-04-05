
package com.cache.eviction;

public interface EvictionPolicy<K> {
    void onPut(K key);
    void onGet(K key);
    K evict();
}
