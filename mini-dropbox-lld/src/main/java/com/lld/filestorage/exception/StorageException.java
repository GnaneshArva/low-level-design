package com.lld.filestorage.exception;

public class StorageException extends FileStorageException {
    public StorageException(final String message) { super(message); }
    public StorageException(final String message, final Throwable cause) { super(message, cause); }
}
