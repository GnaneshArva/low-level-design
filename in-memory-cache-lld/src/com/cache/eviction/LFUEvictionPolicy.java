
package com.cache.eviction;

import java.util.HashMap;
import java.util.Map;

public class LFUEvictionPolicy<K> implements EvictionPolicy<K> {

    private final Map<K, Integer> frequency = new HashMap<>();

    @Override
    public void onPut(K key) {
        frequency.put(key, 1);
    }

    @Override
    public void onGet(K key) {
        frequency.put(key, frequency.getOrDefault(key, 0) + 1);
    }

    @Override
    public K evict() {
        K minKey = null;
        int min = Integer.MAX_VALUE;
        for (Map.Entry<K, Integer> entry : frequency.entrySet()) {
            if (entry.getValue() < min) {
                min = entry.getValue();
                minKey = entry.getKey();
            }
        }
        frequency.remove(minKey);
        return minKey;
    }
}
