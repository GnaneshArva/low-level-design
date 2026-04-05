
package com.cache.storage;

import com.cache.core.CacheEntry;

import java.util.HashMap;
import java.util.Map;

public class CacheStorage<K, V> {

    private final Map<K, CacheEntry<K, V>> storage = new HashMap<>();

    public CacheEntry<K, V> get(K key) {
        return storage.get(key);
    }

    public void put(K key, CacheEntry<K, V> entry) {
        storage.put(key, entry);
    }

    public void remove(K key) {
        storage.remove(key);
    }

    public int size() {
        return storage.size();
    }

    public Map<K, CacheEntry<K, V>> getAll() {
        return storage;
    }

    public void clear() {
        storage.clear();
    }
}
