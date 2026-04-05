
package com.logging;

import com.logging.appender.ConsoleAppender;
import com.logging.appender.FileAppender;
import com.logging.factory.LoggerFactory;
import com.logging.filter.LogFilter;
import com.logging.formatter.JsonFormatter;
import com.logging.formatter.SimpleFormatter;
import com.logging.level.LogLevel;
import com.logging.logger.Logger;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        ConsoleAppender consoleAppender = new ConsoleAppender(new SimpleFormatter());
        FileAppender fileAppender = new FileAppender(
                "application.log",
                new JsonFormatter(),
                1024 * 1024
        );

        Logger logger = LoggerFactory.createLogger(
                Arrays.asList(consoleAppender, fileAppender),
                new LogFilter(LogLevel.DEBUG),
                true
        );

        logger.log(LogLevel.INFO, "Application started");
        logger.log(LogLevel.DEBUG, "Debugging details");
        logger.log(LogLevel.ERROR, "Something failed");
    }
}
