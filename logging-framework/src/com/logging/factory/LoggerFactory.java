
package com.logging.factory;

import com.logging.appender.Appender;
import com.logging.filter.LogFilter;
import com.logging.logger.AsyncLogger;
import com.logging.logger.DefaultLogger;
import com.logging.logger.Logger;

import java.util.List;

public class LoggerFactory {

    public static Logger createLogger(List<Appender> appenders, LogFilter filter, boolean async) {
        Logger logger = new DefaultLogger(appenders, filter);
        return async ? new AsyncLogger(logger) : logger;
    }
}
