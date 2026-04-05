
package com.cache.eviction;

import java.util.LinkedHashSet;

public class LRUEvictionPolicy<K> implements EvictionPolicy<K> {

    private final LinkedHashSet<K> order = new LinkedHashSet<>();

    @Override
    public void onPut(K key) {
        order.remove(key);
        order.add(key);
    }

    @Override
    public void onGet(K key) {
        order.remove(key);
        order.add(key);
    }

    @Override
    public K evict() {
        K first = order.iterator().next();
        order.remove(first);
        return first;
    }
}
