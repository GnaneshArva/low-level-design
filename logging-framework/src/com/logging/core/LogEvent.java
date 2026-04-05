
package com.logging.core;

import com.logging.level.LogLevel;

public final class LogEvent {
    private final long timestamp;
    private final LogLevel level;
    private final String message;
    private final String threadName;

    public LogEvent(long timestamp, LogLevel level, String message, String threadName) {
        this.timestamp = timestamp;
        this.level = level;
        this.message = message;
        this.threadName = threadName;
    }

    public long getTimestamp() { return timestamp; }
    public LogLevel getLevel() { return level; }
    public String getMessage() { return message; }
    public String getThreadName() { return threadName; }
}
