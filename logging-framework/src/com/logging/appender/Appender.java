
package com.logging.appender;

import com.logging.core.LogEvent;

public interface Appender {
    void append(LogEvent event);
}
