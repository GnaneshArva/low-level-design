package com.lld.filestorage.storage;

/**
 * Placeholder for a cloud-backed storage strategy (e.g. AWS S3, GCS).
 *
 * OCP demonstration: adding cloud support requires ONLY this new class.
 * The entire service layer remains untouched.
 *
 * In a real implementation:
 *   - Inject an S3Client (or GCS Storage client) via constructor.
 *   - store()    → s3Client.putObject(bucket, storageKey, content)
 *   - retrieve() → s3Client.getObject(bucket, storageKey)
 *   - delete()   → s3Client.deleteObject(bucket, storageKey)
 *
 * Marked as a stub — throws UnsupportedOperationException to make it explicit
 * rather than silently failing.
 */
public final class CloudStorageStrategy implements StorageStrategy {

    private final String bucketName;
    // private final S3Client s3Client;  // injected in production

    public CloudStorageStrategy(final String bucketName) {
        if (bucketName == null || bucketName.isBlank())
            throw new IllegalArgumentException("bucketName required");
        this.bucketName = bucketName;
    }

    @Override
    public void store(final String storageKey, final byte[] content) {
        // s3Client.putObject(PutObjectRequest.builder().bucket(bucketName).key(storageKey).build(), ...)
        throw new UnsupportedOperationException("CloudStorageStrategy is a stub. Inject an S3Client to activate.");
    }

    @Override
    public byte[] retrieve(final String storageKey) {
        throw new UnsupportedOperationException("CloudStorageStrategy is a stub.");
    }

    @Override
    public void delete(final String storageKey) {
        throw new UnsupportedOperationException("CloudStorageStrategy is a stub.");
    }
}
