
package com.logging.formatter;

import com.logging.core.LogEvent;

public interface Formatter {
    String format(LogEvent event);
}
