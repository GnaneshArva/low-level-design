
package com.cache;

import com.cache.api.Cache;
import com.cache.builder.CacheBuilder;
import com.cache.config.CacheConfig;
import com.cache.eviction.LFUEvictionPolicy;
import com.cache.eviction.LRUEvictionPolicy;
import com.cache.factory.CacheFactory;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        CacheConfig<String> config = new CacheBuilder<String>()
                .capacity(3)
                .ttl(2000)
                .evictionPolicy(new LRUEvictionPolicy<>())
                .build();

        Cache<String, String> cache = CacheFactory.create(config);

        cache.put("A", "Apple");
        cache.put("B", "Ball");
        cache.put("C", "Cat");

        cache.get("A");

        cache.put("D", "Dog");

        System.out.println(cache.get("B")); // should be null (evicted)

        Thread.sleep(2500);

        System.out.println(cache.get("A")); // expired

        // Switch policy
        CacheConfig<String> lfuConfig = new CacheBuilder<String>()
                .capacity(2)
                .evictionPolicy(new LFUEvictionPolicy<>())
                .build();

        Cache<String, String> lfuCache = CacheFactory.create(lfuConfig);
        lfuCache.put("X", "X-ray");
        lfuCache.put("Y", "Yak");
        lfuCache.get("X");
        lfuCache.put("Z", "Zebra");

        System.out.println(lfuCache.get("Y")); // LFU eviction
    }
}
