package ratelimiter.exception;

/**
 * Thrown when rate limit configuration is invalid.
 * Distinct from IllegalArgumentException to allow precise catch blocks.
 */
public class InvalidConfigException extends RuntimeException {
    public InvalidConfigException(String message) {
        super(message);
    }
}
