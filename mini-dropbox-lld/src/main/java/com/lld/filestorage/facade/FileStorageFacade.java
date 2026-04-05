package com.lld.filestorage.facade;

import com.lld.filestorage.domain.AccessType;
import com.lld.filestorage.domain.FileMetadata;
import com.lld.filestorage.domain.FileSystemNode;
import com.lld.filestorage.domain.FileVersion;
import com.lld.filestorage.domain.Folder;
import com.lld.filestorage.domain.Permission;
import com.lld.filestorage.domain.User;
import com.lld.filestorage.observer.AuditLogListener;
import com.lld.filestorage.observer.FileEventPublisher;
import com.lld.filestorage.service.FileStorageService;
import com.lld.filestorage.service.VersionIdGenerator;
import com.lld.filestorage.storage.StorageStrategy;

import java.util.List;

/**
 * Facade Pattern — single entry point for all file storage operations.
 *
 * SRP: Wires up the dependency graph (root folder, storage, publisher, service)
 *      and exposes a simplified, client-friendly API.
 *
 * DIP: Accepts StorageStrategy via constructor — caller decides the backend.
 *      This makes swapping InMemory ↔ Local ↔ Cloud trivial at construction time.
 *
 * Trade-off: A full application would use a DI framework (Spring) here.
 *            Manual wiring is intentional for interview clarity — no magic.
 */
public final class FileStorageFacade {

    private final FileStorageService service;

    public FileStorageFacade(final StorageStrategy storageStrategy) {
        if (storageStrategy == null) throw new IllegalArgumentException("storageStrategy required");

        // Wire up the dependency graph
        final Folder             root       = new Folder("root", new User("system", "System"));
        final FileEventPublisher publisher  = new FileEventPublisher();
        final VersionIdGenerator versionGen = new VersionIdGenerator();

        // Register default audit listener — additional listeners can be added via getPublisher()
        publisher.subscribe(new AuditLogListener());

        this.service = new FileStorageService(root, storageStrategy, publisher, versionGen);
    }

    // ── Delegating API ───────────────────────────────────────────────────────

    public FileVersion upload(final FileMetadata metadata) {
        return service.upload(metadata);
    }

    public byte[] download(final User requester, final String folderPath, final String fileName) {
        return service.download(requester, folderPath, fileName);
    }

    public Folder createFolder(final String parentPath, final String folderName, final User owner) {
        return service.createFolder(parentPath, folderName, owner);
    }

    public void delete(final User requester, final String folderPath, final String targetName) {
        service.delete(requester, folderPath, targetName);
    }

    public Permission share(final User sharer,
                            final String folderPath,
                            final String nodeName,
                            final User recipient,
                            final AccessType accessType) {
        return service.share(sharer, folderPath, nodeName, recipient, accessType);
    }

    public List<FileSystemNode> list(final User requester, final String folderPath) {
        return service.list(requester, folderPath);
    }

    public List<FileVersion> listVersions(final User requester,
                                          final String folderPath,
                                          final String fileName) {
        return service.listVersions(requester, folderPath, fileName);
    }
}
