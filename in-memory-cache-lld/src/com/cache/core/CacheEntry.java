
package com.cache.core;

public class CacheEntry<K, V> {

    private final K key;
    private V value;
    private long expiryTime;
    private long lastAccessTime;
    private int frequency;

    public CacheEntry(K key, V value, long ttlMillis) {
        this.key = key;
        this.value = value;
        this.expiryTime = ttlMillis > 0 ? System.currentTimeMillis() + ttlMillis : -1;
        this.lastAccessTime = System.currentTimeMillis();
        this.frequency = 1;
    }

    public K getKey() { return key; }

    public V getValue() { return value; }

    public void setValue(V value) { this.value = value; }

    public long getExpiryTime() { return expiryTime; }

    public long getLastAccessTime() { return lastAccessTime; }

    public void updateAccessTime() {
        this.lastAccessTime = System.currentTimeMillis();
    }

    public int getFrequency() { return frequency; }

    public void incrementFrequency() { frequency++; }

    public boolean isExpired() {
        return expiryTime > 0 && System.currentTimeMillis() > expiryTime;
    }
}
