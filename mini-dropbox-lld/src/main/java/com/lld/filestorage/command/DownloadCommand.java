package com.lld.filestorage.command;

import com.lld.filestorage.domain.AccessType;
import com.lld.filestorage.domain.File;
import com.lld.filestorage.domain.FileSystemNode;
import com.lld.filestorage.domain.FileVersion;
import com.lld.filestorage.domain.Folder;
import com.lld.filestorage.domain.User;
import com.lld.filestorage.exception.NodeNotFoundException;
import com.lld.filestorage.exception.PermissionDeniedException;
import com.lld.filestorage.observer.FileEvent;
import com.lld.filestorage.observer.FileEventPublisher;
import com.lld.filestorage.service.FolderNavigator;
import com.lld.filestorage.storage.StorageStrategy;

/**
 * Command Pattern — encapsulates a download operation.
 *
 * Downloads the latest version of a file. The storage key is reconstructed
 * from the version metadata stored in the File domain object.
 */
public final class DownloadCommand implements FileCommand {

    private final User               requester;
    private final String             folderPath;
    private final String             fileName;
    private final Folder             root;
    private final StorageStrategy    storage;
    private final FileEventPublisher publisher;

    public DownloadCommand(final User requester,
                           final String folderPath,
                           final String fileName,
                           final Folder root,
                           final StorageStrategy storage,
                           final FileEventPublisher publisher) {
        this.requester  = requester;
        this.folderPath = folderPath;
        this.fileName   = fileName;
        this.root       = root;
        this.storage    = storage;
        this.publisher  = publisher;
    }

    /**
     * @return byte[] content of the latest file version.
     */
    @Override
    public byte[] execute() {
        // 1. Navigate to folder
        final Folder folder = FolderNavigator.navigate(root, folderPath);

        // 2. Find file node
        final FileSystemNode node = folder.findChild(fileName)
                .orElseThrow(() -> new NodeNotFoundException(
                        "File '" + fileName + "' not found in '" + folderPath + "'"));

        if (node.isFolder()) {
            throw new IllegalArgumentException("'" + fileName + "' is a folder, not a file.");
        }

        final File fileNode = (File) node;

        // 3. Permission check — READ access required
        if (!fileNode.hasAccess(requester, AccessType.READ)) {
            throw new PermissionDeniedException(
                    "User '" + requester.getUserId() + "' lacks READ access on '" + fileName + "'");
        }

        // 4. Get latest version
        final FileVersion latest = fileNode.getLatestVersion()
                .orElseThrow(() -> new NodeNotFoundException("File '" + fileName + "' has no content."));

        // 5. Build storage key (matches pattern from UploadCommand)
        final String storageKey = buildStorageKey(fileNode.getOwner().getUserId(),
                folderPath, fileName, latest.getVersionId());

        final byte[] content = storage.retrieve(storageKey);

        // 6. Publish event
        publisher.publish(new FileEvent(
                FileEvent.EventType.DOWNLOADED,
                folderPath + "/" + fileName,
                requester.getUserId()));

        return content;
    }

    private String buildStorageKey(final String ownerUserId,
                                   final String folderPath,
                                   final String fileName,
                                   final String versionId) {
        final String normPath = folderPath.startsWith("/") ? folderPath.substring(1) : folderPath;
        return ownerUserId + "/" + normPath + "/" + fileName + "@" + versionId;
    }
}
