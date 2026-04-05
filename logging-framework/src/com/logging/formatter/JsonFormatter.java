
package com.logging.formatter;

import com.logging.core.LogEvent;

public class JsonFormatter implements Formatter {

    @Override
    public String format(LogEvent event) {
        return "{"
                + ""timestamp":" + event.getTimestamp() + ","
                + ""thread":"" + event.getThreadName() + "","
                + ""level":"" + event.getLevel() + "","
                + ""message":"" + event.getMessage() + """
                + "}";
    }
}
