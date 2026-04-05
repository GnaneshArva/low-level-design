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
 * Command Pattern — encapsulates a delete operation.
 *
 * Business rule: deleting a folder recursively deletes all children.
 * Only the owner or a user with WRITE access can delete a node.
 */
public final class DeleteCommand implements FileCommand {

    private final User               requester;
    private final String             folderPath;   // parent folder path
    private final String             targetName;   // name of node to delete
    private final Folder             root;
    private final StorageStrategy    storage;
    private final FileEventPublisher publisher;

    public DeleteCommand(final User requester,
                         final String folderPath,
                         final String targetName,
                         final Folder root,
                         final StorageStrategy storage,
                         final FileEventPublisher publisher) {
        this.requester  = requester;
        this.folderPath = folderPath;
        this.targetName = targetName;
        this.root       = root;
        this.storage    = storage;
        this.publisher  = publisher;
    }

    /**
     * @return the name of the deleted node (confirmation token).
     */
    @Override
    public String execute() {
        final Folder parentFolder = FolderNavigator.navigate(root, folderPath);

        final FileSystemNode target = parentFolder.findChild(targetName)
                .orElseThrow(() -> new NodeNotFoundException(
                        "'" + targetName + "' not found in '" + folderPath + "'"));

        // Only owner or WRITE-access user may delete
        if (!target.hasAccess(requester, AccessType.WRITE)) {
            throw new PermissionDeniedException(
                    "User '" + requester.getUserId() + "' cannot delete '" + targetName + "'");
        }

        // Recursively purge storage bytes before removing from tree
        purgeStorage(target, folderPath);

        parentFolder.removeChild(targetName);

        publisher.publish(new FileEvent(
                FileEvent.EventType.DELETED,
                folderPath + "/" + targetName,
                requester.getUserId()));

        return targetName;
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    /** Depth-first recursive purge of all stored bytes under this node. */
    private void purgeStorage(final FileSystemNode node, final String currentPath) {
        if (!node.isFolder()) {
            deleteFileVersions((File) node, currentPath);
            return;
        }
        final Folder folder   = (Folder) node;
        final String childPath = currentPath + "/" + node.getName();
        for (FileSystemNode child : folder.getChildren()) {
            purgeStorage(child, childPath);
        }
    }

    private void deleteFileVersions(final File file, final String parentPath) {
        final String ownerUserId = file.getOwner().getUserId();
        final String normPath    = parentPath.startsWith("/") ? parentPath.substring(1) : parentPath;
        for (FileVersion version : file.getAllVersions()) {
            final String key = ownerUserId + "/" + normPath + "/" + file.getName() + "@" + version.getVersionId();
            storage.delete(key);
        }
    }
}
