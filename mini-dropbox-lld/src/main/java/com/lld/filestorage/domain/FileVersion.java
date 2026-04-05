package com.lld.filestorage.domain;

import java.time.Instant;

/**
 * Immutable snapshot of file content at a point in time.
 * SRP: Only models a version — no upload/download logic here.
 * Immutability guarantees consistency once a version is created.
 */
public final class FileVersion {

    private final String  versionId;
    private final byte[]  content;        // defensive copy on construction
    private final long    sizeBytes;
    private final Instant timestamp;

    public FileVersion(final String versionId, final byte[] content) {
        if (versionId == null || versionId.isBlank()) throw new IllegalArgumentException("versionId required");
        if (content == null)                          throw new IllegalArgumentException("content must not be null");
        this.versionId  = versionId;
        this.content    = content.clone();   // defensive copy — immutability
        this.sizeBytes  = content.length;
        this.timestamp  = Instant.now();
    }

    public String  getVersionId()  { return versionId; }
    public byte[]  getContent()    { return content.clone(); }  // defensive copy on read
    public long    getSizeBytes()  { return sizeBytes; }
    public Instant getTimestamp()  { return timestamp; }

    @Override
    public String toString() {
        return "FileVersion{id=" + versionId + ", size=" + sizeBytes + "B, ts=" + timestamp + "}";
    }
}
