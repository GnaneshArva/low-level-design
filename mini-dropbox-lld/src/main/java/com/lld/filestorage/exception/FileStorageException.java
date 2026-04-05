package com.lld.filestorage.exception;

public class FileStorageException extends RuntimeException {
    public FileStorageException(final String message) { super(message); }
    public FileStorageException(final String message, final Throwable cause) { super(message, cause); }
}
