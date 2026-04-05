package ratelimiter.algorithm;

import ratelimiter.config.RateLimitConfig;
import ratelimiter.core.RequestContext;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Sliding Window Log Algorithm:
 * - Stores actual timestamps of each request in a per-user log (deque).
 * - On each request, evicts timestamps older than windowSize.
 * - Allows if the remaining log size is below the limit.
 *
 * Trade-off: More accurate than Fixed Window (no boundary burst problem).
 * Memory: O(limit) per user — bounded by eviction.
 * Cost: O(N) eviction per request in worst case, acceptable in practice.
 */
public final class SlidingWindowStrategy implements RateLimitStrategy {

    private final RateLimitConfig config;
    private final Map<String, Deque<Long>> requestLog = new ConcurrentHashMap<>();

    public SlidingWindowStrategy(RateLimitConfig config) {
        if (config == null) throw new IllegalArgumentException("Config must not be null");
        this.config = config;
    }

    @Override
    public synchronized boolean allowRequest(RequestContext context) {
        final String userId = context.getUserId();
        final long now = context.getTimestampMs();
        final long windowStart = now - config.getWindowSizeMs();

        Deque<Long> log = requestLog.computeIfAbsent(userId, k -> new ArrayDeque<>());

        // Evict timestamps that fall outside the current sliding window
        while (!log.isEmpty() && log.peekFirst() <= windowStart) {
            log.pollFirst();
        }

        if (log.size() < config.getLimit()) {
            log.addLast(now);
            return true;
        }

        return false;
    }

    @Override
    public int getRemainingTokens(String userId) {
        Deque<Long> log = requestLog.get(userId);
        if (log == null) return config.getLimit();
        long windowStart = System.currentTimeMillis() - config.getWindowSizeMs();
        long active = log.stream().filter(t -> t > windowStart).count();
        return Math.max(0, config.getLimit() - (int) active);
    }

    @Override
    public String algorithmName() {
        return "SlidingWindowLog";
    }
}
