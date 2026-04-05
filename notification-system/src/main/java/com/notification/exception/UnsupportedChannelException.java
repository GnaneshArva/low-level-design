package com.notification.exception;

/**
 * Thrown when an unsupported or unregistered channel type is requested.
 * Maps to OCP violation detection — missing factory registration is caught here.
 */
public class UnsupportedChannelException extends RuntimeException {

    public UnsupportedChannelException(String channelType) {
        super("No channel implementation registered for type: " + channelType);
    }
}
