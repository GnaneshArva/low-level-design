
package com.logging.filter;

import com.logging.level.LogLevel;

public class LogFilter {

    private final LogLevel minLevel;

    public LogFilter(LogLevel minLevel) {
        this.minLevel = minLevel;
    }

    public boolean shouldLog(LogLevel level) {
        return level.getPriority() >= minLevel.getPriority();
    }
}
