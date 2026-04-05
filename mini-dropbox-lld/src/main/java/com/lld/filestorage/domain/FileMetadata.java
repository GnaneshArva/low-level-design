package com.lld.filestorage.domain;

/**
 * Immutable metadata bag for a file upload request.
 *
 * Builder Pattern: upload callers construct metadata via a fluent builder,
 * avoiding telescoping constructors and making optional fields explicit.
 *
 * SRP: Only carries metadata — no I/O or business logic.
 */
public final class FileMetadata {

    private final String  fileName;
    private final String  mimeType;
    private final String  targetFolderPath;
    private final User    uploader;
    private final byte[]  content;

    private FileMetadata(final Builder builder) {
        this.fileName         = builder.fileName;
        this.mimeType         = builder.mimeType;
        this.targetFolderPath = builder.targetFolderPath;
        this.uploader         = builder.uploader;
        this.content          = builder.content.clone();
    }

    public String  getFileName()         { return fileName; }
    public String  getMimeType()         { return mimeType; }
    public String  getTargetFolderPath() { return targetFolderPath; }
    public User    getUploader()         { return uploader; }
    public byte[]  getContent()          { return content.clone(); }
    public long    getSizeBytes()        { return content.length; }

    // ── Builder ──────────────────────────────────────────────────────────────

    public static Builder builder() { return new Builder(); }

    public static final class Builder {

        private String fileName;
        private String mimeType         = "application/octet-stream";
        private String targetFolderPath = "/";
        private User   uploader;
        private byte[] content;

        public Builder fileName(final String fileName)               { this.fileName = fileName;               return this; }
        public Builder mimeType(final String mimeType)               { this.mimeType = mimeType;               return this; }
        public Builder targetFolderPath(final String path)           { this.targetFolderPath = path;           return this; }
        public Builder uploader(final User uploader)                 { this.uploader = uploader;               return this; }
        public Builder content(final byte[] content)                 { this.content = content;                 return this; }

        public FileMetadata build() {
            if (fileName == null || fileName.isBlank()) throw new IllegalStateException("fileName required");
            if (uploader == null)                       throw new IllegalStateException("uploader required");
            if (content  == null)                       throw new IllegalStateException("content required");
            return new FileMetadata(this);
        }
    }

    @Override
    public String toString() {
        return "FileMetadata{file='" + fileName + "', path='" + targetFolderPath
                + "', uploader=" + uploader.getUserId() + ", size=" + content.length + "B}";
    }
}
