
package com.logging.appender;

import com.logging.core.LogEvent;
import com.logging.exception.LoggingException;
import com.logging.formatter.Formatter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileAppender implements Appender {

    private final Formatter formatter;
    private final File file;
    private final long maxSizeBytes;

    public FileAppender(String filePath, Formatter formatter, long maxSizeBytes) {
        this.file = new File(filePath);
        this.formatter = formatter;
        this.maxSizeBytes = maxSizeBytes;
    }

    @Override
    public synchronized void append(LogEvent event) {
        try {
            rotateIfNeeded();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write(formatter.format(event));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new LoggingException("Failed writing log file", e);
        }
    }

    private void rotateIfNeeded() {
        if (file.exists() && file.length() >= maxSizeBytes) {
            File rotated = new File(file.getAbsolutePath() + "." + System.currentTimeMillis());
            if (!file.renameTo(rotated)) {
                throw new LoggingException("Log rotation failed", null);
            }
        }
    }
}
