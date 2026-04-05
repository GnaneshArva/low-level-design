package com.lld.filestorage.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Composite Pattern — Leaf node.
 * Maintains an ordered version history; the latest version is the current state.
 *
 * SRP: File manages its own metadata and version list.
 *      Storage I/O is delegated to StorageStrategy (DIP).
 */
public final class File extends FileSystemNode {

    // Ordered list: index 0 = first upload, last = latest version.
    private final List<FileVersion> versions = new ArrayList<>();

    public File(final String name, final User owner) {
        super(name, owner);
    }

    // ── Version management ───────────────────────────────────────────────────

    public void addVersion(final FileVersion version) {
        if (version == null) throw new IllegalArgumentException("version must not be null");
        versions.add(version);
    }

    /** Returns the most recent version, or empty if the file has never been uploaded. */
    public Optional<FileVersion> getLatestVersion() {
        if (versions.isEmpty()) return Optional.empty();
        return Optional.of(versions.get(versions.size() - 1));
    }

    public List<FileVersion> getAllVersions() {
        return Collections.unmodifiableList(versions);
    }

    public int getVersionCount() { return versions.size(); }

    /** Convenience: total size of the latest version, or 0 if none. */
    public long getSizeBytes() {
        return getLatestVersion().map(FileVersion::getSizeBytes).orElse(0L);
    }

    // ── Composite ────────────────────────────────────────────────────────────

    @Override public boolean isFolder() { return false; }
}
