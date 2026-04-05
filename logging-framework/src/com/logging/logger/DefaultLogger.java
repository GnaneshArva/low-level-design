
package com.logging.logger;

import com.logging.appender.Appender;
import com.logging.builder.LogEventBuilder;
import com.logging.core.LogEvent;
import com.logging.filter.LogFilter;
import com.logging.level.LogLevel;

import java.util.List;

public class DefaultLogger implements Logger {

    private final List<Appender> appenders;
    private final LogFilter filter;

    public DefaultLogger(List<Appender> appenders, LogFilter filter) {
        this.appenders = appenders;
        this.filter = filter;
    }

    @Override
    public void log(LogLevel level, String message) {
        if (!filter.shouldLog(level)) {
            return;
        }

        LogEvent event = new LogEventBuilder()
                .timestamp(System.currentTimeMillis())
                .level(level)
                .message(message)
                .threadName(Thread.currentThread().getName())
                .build();

        for (Appender appender : appenders) {
            appender.append(event);
        }
    }
}
