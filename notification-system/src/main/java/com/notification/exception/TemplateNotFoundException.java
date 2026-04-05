package com.notification.exception;

/**
 * Thrown when a requested template key has no registered template.
 */
public class TemplateNotFoundException extends RuntimeException {

    public TemplateNotFoundException(String templateKey) {
        super("No template found for key: " + templateKey);
    }
}
