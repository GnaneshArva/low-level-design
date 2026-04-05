
package com.logging.exception;

public class LoggingException extends RuntimeException {
    public LoggingException(String message, Throwable cause) {
        super(message, cause);
    }
}
