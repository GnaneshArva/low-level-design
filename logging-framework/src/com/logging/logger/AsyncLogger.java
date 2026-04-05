
package com.logging.logger;

import com.logging.level.LogLevel;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AsyncLogger implements Logger {

    private final Logger delegate;
    private final BlockingQueue<Runnable> queue;
    private final Thread worker;

    public AsyncLogger(Logger delegate) {
        this.delegate = delegate;
        this.queue = new LinkedBlockingQueue<>();
        this.worker = new Thread(this::process);
        this.worker.setDaemon(true);
        this.worker.start();
    }

    @Override
    public void log(LogLevel level, String message) {
        queue.offer(() -> delegate.log(level, message));
    }

    private void process() {
        while (true) {
            try {
                Runnable task = queue.take();
                task.run();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
