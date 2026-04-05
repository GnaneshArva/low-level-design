package ratelimiter.demo;

import ratelimiter.config.RateLimitConfig;
import ratelimiter.core.RateLimitResult;
import ratelimiter.core.RateLimiterFacade;
import ratelimiter.factory.AlgorithmType;

/**
 * Demonstration of the Rate Limiter system.
 *
 * Shows:
 * 1. Fixed Window — per-user exhaustion
 * 2. Global limit — system-wide cap across users
 * 3. Algorithm hot-swap at runtime
 * 4. Remaining quota visibility
 */
public class RateLimiterDemo {

    public static void main(String[] args) throws InterruptedException {

        // ================================================================
        // DEMO 1: Fixed Window — per-user rate limiting (limit: 3 req/5s)
        // ================================================================
        System.out.println("===========================================");
        System.out.println("DEMO 1: Fixed Window Per-User Rate Limiting");
        System.out.println("===========================================");

        RateLimitConfig userConfig = new RateLimitConfig.Builder()
                .limit(3)
                .windowSizeMs(5000)    // 5-second window
                .refillRate(0)
                .build();

        RateLimitConfig globalConfig = new RateLimitConfig.Builder()
                .limit(10)
                .windowSizeMs(5000)
                .refillRate(0)
                .build();

        RateLimiterFacade limiter = new RateLimiterFacade(AlgorithmType.FIXED_WINDOW, userConfig, globalConfig);

        String userAlice = "alice";
        String userBob = "bob";

        // Alice sends 4 requests — 4th should be denied
        for (int i = 1; i <= 4; i++) {
            RateLimitResult result = limiter.allowRequest(userAlice);
            System.out.printf("Alice request %d → %s%n", i, result);
        }

        System.out.println();

        // Bob sends 2 requests — all allowed (independent from Alice)
        for (int i = 1; i <= 2; i++) {
            RateLimitResult result = limiter.allowRequest(userBob);
            System.out.printf("Bob   request %d → %s%n", i, result);
        }

        // ================================================================
        // DEMO 2: Global Rate Limiting
        // ================================================================
        System.out.println("\n===========================================");
        System.out.println("DEMO 2: Global Rate Limit (cap=5 across all users)");
        System.out.println("===========================================");

        RateLimitConfig smallUserConfig = new RateLimitConfig.Builder()
                .limit(10)
                .windowSizeMs(5000)
                .refillRate(0)
                .build();

        RateLimitConfig smallGlobalConfig = new RateLimitConfig.Builder()
                .limit(5)              // only 5 requests allowed system-wide
                .windowSizeMs(5000)
                .refillRate(0)
                .build();

        RateLimiterFacade globalLimiter = new RateLimiterFacade(
                AlgorithmType.FIXED_WINDOW, smallUserConfig, smallGlobalConfig);

        String[] users = {"user1", "user2", "user3"};
        int count = 0;
        for (String user : users) {
            for (int i = 1; i <= 3; i++) {
                count++;
                RateLimitResult result = globalLimiter.allowRequest(user);
                System.out.printf("Request %2d (%s) → %s%n", count, user, result);
            }
        }

        // ================================================================
        // DEMO 3: Token Bucket — burst handling
        // ================================================================
        System.out.println("\n===========================================");
        System.out.println("DEMO 3: Token Bucket (burst=5, refill=1/s)");
        System.out.println("===========================================");

        RateLimitConfig bucketUser = new RateLimitConfig.Builder()
                .limit(5)
                .windowSizeMs(10000)
                .refillRate(1)         // 1 token per second
                .build();

        RateLimitConfig bucketGlobal = new RateLimitConfig.Builder()
                .limit(100)
                .windowSizeMs(10000)
                .refillRate(10)
                .build();

        RateLimiterFacade bucketLimiter = new RateLimiterFacade(
                AlgorithmType.TOKEN_BUCKET, bucketUser, bucketGlobal);

        // Burst of 7 — first 5 pass, rest denied
        for (int i = 1; i <= 7; i++) {
            RateLimitResult result = bucketLimiter.allowRequest("charlie");
            System.out.printf("Charlie request %d → %s%n", i, result);
        }

        // Wait 2 seconds for tokens to refill
        System.out.println("\n[Waiting 2 seconds for token refill...]");
        Thread.sleep(2000);

        // Should allow ~2 more (refill at 1/s for 2s)
        for (int i = 1; i <= 3; i++) {
            RateLimitResult result = bucketLimiter.allowRequest("charlie");
            System.out.printf("Charlie after-refill request %d → %s%n", i, result);
        }

        // ================================================================
        // DEMO 4: Algorithm Hot-Swap
        // ================================================================
        System.out.println("\n===========================================");
        System.out.println("DEMO 4: Runtime Algorithm Switch");
        System.out.println("===========================================");

        RateLimitConfig swapConfig = new RateLimitConfig.Builder()
                .limit(3)
                .windowSizeMs(5000)
                .refillRate(1)
                .build();

        RateLimiterFacade swappable = new RateLimiterFacade(
                AlgorithmType.FIXED_WINDOW, swapConfig, globalConfig);

        System.out.println("--- Using Fixed Window ---");
        for (int i = 1; i <= 2; i++) {
            System.out.printf("dave request %d → %s%n", i, swappable.allowRequest("dave"));
        }

        swappable.switchAlgorithm(AlgorithmType.SLIDING_WINDOW);

        System.out.println("--- Switched to Sliding Window ---");
        for (int i = 1; i <= 2; i++) {
            System.out.printf("dave request %d → %s%n", i, swappable.allowRequest("dave"));
        }

        swappable.switchAlgorithm(AlgorithmType.TOKEN_BUCKET);

        System.out.println("--- Switched to Token Bucket ---");
        for (int i = 1; i <= 2; i++) {
            System.out.printf("dave request %d → %s%n", i, swappable.allowRequest("dave"));
        }

        System.out.println("\n===========================================");
        System.out.println("DEMO COMPLETE");
        System.out.println("===========================================");
    }
}
