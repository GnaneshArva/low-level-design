
package com.logging.appender;

import com.logging.core.LogEvent;
import com.logging.formatter.Formatter;

public class ConsoleAppender implements Appender {

    private final Formatter formatter;

    public ConsoleAppender(Formatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public void append(LogEvent event) {
        System.out.println(formatter.format(event));
    }
}
