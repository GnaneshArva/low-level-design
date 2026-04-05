
package com.logging.formatter;

import com.logging.core.LogEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleFormatter implements Formatter {

    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Override
    public String format(LogEvent event) {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
        String time = sdf.format(new Date(event.getTimestamp()));
        return time + " [" + event.getThreadName() + "] "
                + event.getLevel() + " - " + event.getMessage();
    }
}
