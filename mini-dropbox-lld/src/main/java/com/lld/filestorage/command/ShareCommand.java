package com.lld.filestorage.command;

import com.lld.filestorage.domain.AccessType;
import com.lld.filestorage.domain.FileSystemNode;
import com.lld.filestorage.domain.Folder;
import com.lld.filestorage.domain.Permission;
import com.lld.filestorage.domain.User;
import com.lld.filestorage.exception.NodeNotFoundException;
import com.lld.filestorage.exception.PermissionDeniedException;
import com.lld.filestorage.observer.FileEvent;
import com.lld.filestorage.observer.FileEventPublisher;
import com.lld.filestorage.service.FolderNavigator;

/**
 * Command Pattern — encapsulates a share operation.
 *
 * Business rules:
 *   - Only the owner or a WRITE user can share a node.
 *   - Sharing a Folder propagates the permission to all children (via Folder.propagatePermissions).
 *   - Sharing a File adds the permission only to that file node.
 */
public final class ShareCommand implements FileCommand {

    private final User               sharer;       // the user granting access
    private final String             folderPath;
    private final String             nodeName;
    private final User               recipient;
    private final AccessType         accessType;
    private final Folder             root;
    private final FileEventPublisher publisher;

    public ShareCommand(final User sharer,
                        final String folderPath,
                        final String nodeName,
                        final User recipient,
                        final AccessType accessType,
                        final Folder root,
                        final FileEventPublisher publisher) {
        this.sharer     = sharer;
        this.folderPath = folderPath;
        this.nodeName   = nodeName;
        this.recipient  = recipient;
        this.accessType = accessType;
        this.root       = root;
        this.publisher  = publisher;
    }

    /**
     * @return the Permission that was granted.
     */
    @Override
    public Permission execute() {
        final Folder parentFolder = FolderNavigator.navigate(root, folderPath);

        final FileSystemNode target = parentFolder.findChild(nodeName)
                .orElseThrow(() -> new NodeNotFoundException(
                        "'" + nodeName + "' not found in '" + folderPath + "'"));

        // Only owner or WRITE user may share
        if (!target.hasAccess(sharer, AccessType.WRITE)) {
            throw new PermissionDeniedException(
                    "User '" + sharer.getUserId() + "' cannot share '" + nodeName + "'");
        }

        final Permission permission = new Permission(recipient, accessType);

        if (target.isFolder()) {
            // Propagates down the entire subtree — business rule for folder sharing
            ((Folder) target).propagatePermissions(permission);
        } else {
            target.addPermission(permission);
        }

        publisher.publish(new FileEvent(
                FileEvent.EventType.SHARED,
                folderPath + "/" + nodeName,
                sharer.getUserId()));

        return permission;
    }
}
