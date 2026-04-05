package com.lld.filestorage.storage;

import com.lld.filestorage.exception.StorageException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Disk-backed storage strategy.
 *
 * OCP: Swapped in place of InMemoryStorageStrategy without touching any service code.
 * Trade-off: synchronous I/O; a production version would use async NIO channels.
 *
 * storageKey is mapped to a flat file path under a configurable base directory.
 * Keys may contain '/' which is treated as a path separator.
 */
public final class LocalStorageStrategy implements StorageStrategy {

    private final Path baseDir;

    public LocalStorageStrategy(final String baseDirectory) {
        if (baseDirectory == null || baseDirectory.isBlank())
            throw new IllegalArgumentException("baseDirectory required");
        this.baseDir = Paths.get(baseDirectory);
    }

    @Override
    public void store(final String storageKey, final byte[] content) {
        try {
            final Path target = resolve(storageKey);
            Files.createDirectories(target.getParent());
            Files.write(target, content);
        } catch (IOException e) {
            throw new StorageException("Failed to store: " + storageKey, e);
        }
    }

    @Override
    public byte[] retrieve(final String storageKey) {
        try {
            final Path target = resolve(storageKey);
            if (!Files.exists(target)) {
                throw new StorageException("File not found for key: " + storageKey);
            }
            return Files.readAllBytes(target);
        } catch (IOException e) {
            throw new StorageException("Failed to retrieve: " + storageKey, e);
        }
    }

    @Override
    public void delete(final String storageKey) {
        try {
            Files.deleteIfExists(resolve(storageKey));
        } catch (IOException e) {
            throw new StorageException("Failed to delete: " + storageKey, e);
        }
    }

    private Path resolve(final String storageKey) {
        // Sanitise: replace any OS-specific separators and normalise
        final String sanitised = storageKey.replace('\\', '/');
        return baseDir.resolve(sanitised).normalize();
    }
}
