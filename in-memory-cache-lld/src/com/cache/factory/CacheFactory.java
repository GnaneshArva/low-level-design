
package com.cache.factory;

import com.cache.api.Cache;
import com.cache.config.CacheConfig;
import com.cache.core.InMemoryCache;

public class CacheFactory {

    public static <K, V> Cache<K, V> create(CacheConfig<K> config) {
        return new InMemoryCache<>(config);
    }
}
