
package com.logging.builder;

import com.logging.core.LogEvent;
import com.logging.level.LogLevel;

public class LogEventBuilder {
    private long timestamp;
    private LogLevel level;
    private String message;
    private String threadName;

    public LogEventBuilder timestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public LogEventBuilder level(LogLevel level) {
        this.level = level;
        return this;
    }

    public LogEventBuilder message(String message) {
        this.message = message;
        return this;
    }

    public LogEventBuilder threadName(String threadName) {
        this.threadName = threadName;
        return this;
    }

    public LogEvent build() {
        return new LogEvent(timestamp, level, message, threadName);
    }
}
