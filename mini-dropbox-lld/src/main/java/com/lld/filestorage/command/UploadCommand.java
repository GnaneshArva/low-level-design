package com.lld.filestorage.command;

import com.lld.filestorage.domain.File;
import com.lld.filestorage.domain.FileMetadata;
import com.lld.filestorage.domain.FileVersion;
import com.lld.filestorage.domain.Folder;
import com.lld.filestorage.domain.User;
import com.lld.filestorage.exception.PermissionDeniedException;
import com.lld.filestorage.domain.AccessType;
import com.lld.filestorage.observer.FileEvent;
import com.lld.filestorage.observer.FileEventPublisher;
import com.lld.filestorage.service.FolderNavigator;
import com.lld.filestorage.service.VersionIdGenerator;
import com.lld.filestorage.storage.StorageStrategy;

import java.util.Optional;

/**
 * Command Pattern — encapsulates an upload operation with all its context.
 *
 * SRP: Knows how to upload one file — folder resolution, versioning,
 *      storage delegation, and event publishing — nothing more.
 *
 * Business rules applied here:
 *   - Uploading to an existing file name creates a new version (no rejection).
 *   - Uploader must have WRITE access to the target folder.
 *   - Storage key format: {ownerId}/{folderPath}/{fileName}@{versionId}
 */
public final class UploadCommand implements FileCommand {

    private final FileMetadata       metadata;
    private final Folder             root;
    private final StorageStrategy    storage;
    private final FileEventPublisher publisher;
    private final VersionIdGenerator versionIdGen;

    public UploadCommand(final FileMetadata metadata,
                         final Folder root,
                         final StorageStrategy storage,
                         final FileEventPublisher publisher,
                         final VersionIdGenerator versionIdGen) {
        this.metadata     = metadata;
        this.root         = root;
        this.storage      = storage;
        this.publisher    = publisher;
        this.versionIdGen = versionIdGen;
    }

    /**
     * @return the new FileVersion that was stored.
     */
    @Override
    public FileVersion execute() {
        final User   uploader   = metadata.getUploader();
        final String folderPath = metadata.getTargetFolderPath();
        final String fileName   = metadata.getFileName();

        // 1. Resolve target folder
        final Folder targetFolder = FolderNavigator.navigate(root, folderPath);

        // 2. Permission check — uploader must have WRITE on the folder
        if (!targetFolder.hasAccess(uploader, AccessType.WRITE)) {
            throw new PermissionDeniedException(
                    "User '" + uploader.getUserId() + "' lacks WRITE access on '" + folderPath + "'");
        }

        // 3. Find existing file node or create a new one
        final File fileNode = resolveOrCreateFileNode(targetFolder, fileName, uploader);

        // 4. Create version and store bytes
        final String      versionId = versionIdGen.generate();
        final FileVersion version   = new FileVersion(versionId, metadata.getContent());
        final String      storageKey = buildStorageKey(uploader, folderPath, fileName, versionId);

        storage.store(storageKey, metadata.getContent());
        fileNode.addVersion(version);

        // 5. Publish event
        publisher.publish(new FileEvent(
                FileEvent.EventType.UPLOADED,
                folderPath + "/" + fileName,
                uploader.getUserId()));

        return version;
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private File resolveOrCreateFileNode(final Folder folder,
                                         final String fileName,
                                         final User   owner) {
        final Optional<com.lld.filestorage.domain.FileSystemNode> existing =
                folder.findChild(fileName);

        if (existing.isPresent()) {
            final com.lld.filestorage.domain.FileSystemNode node = existing.get();
            if (node.isFolder()) {
                throw new IllegalArgumentException(
                        "'" + fileName + "' is a folder, not a file.");
            }
            return (File) node;
        }

        // First upload — create the file node and register it in the folder
        final File newFile = new File(fileName, owner);
        folder.addChild(newFile);
        return newFile;
    }

    private String buildStorageKey(final User uploader,
                                   final String folderPath,
                                   final String fileName,
                                   final String versionId) {
        // Normalise leading slash
        final String normPath = folderPath.startsWith("/") ? folderPath.substring(1) : folderPath;
        return uploader.getUserId() + "/" + normPath + "/" + fileName + "@" + versionId;
    }
}
