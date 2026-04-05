package com.lld.filestorage.storage;

/**
 * Strategy Pattern — storage backend abstraction.
 *
 * DIP  : FileStorageService depends on this interface, not on any concrete backend.
 * OCP  : New backends (S3, GCS, Azure Blob) are added by implementing this interface —
 *        zero modification to existing code.
 * ISP  : Three focused methods; implementors are not forced to carry unrelated concerns.
 */
public interface StorageStrategy {

    /**
     * Persists content under a logical storage key.
     * @param storageKey  unique identifier (e.g. "userId/path/file@v1")
     * @param content     raw bytes to store
     */
    void store(String storageKey, byte[] content);

    /**
     * Retrieves content by its storage key.
     * @return raw bytes; never null
     * @throws com.lld.filestorage.exception.StorageException if key not found
     */
    byte[] retrieve(String storageKey);

    /**
     * Removes content by its storage key. Idempotent — no exception if absent.
     */
    void delete(String storageKey);
}
