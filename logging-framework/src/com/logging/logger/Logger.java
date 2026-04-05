
package com.logging.logger;

import com.logging.level.LogLevel;

public interface Logger {
    void log(LogLevel level, String message);
}
