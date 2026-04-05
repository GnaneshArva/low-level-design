package com.lld.filestorage.service;

import com.lld.filestorage.command.DeleteCommand;
import com.lld.filestorage.command.DownloadCommand;
import com.lld.filestorage.command.FileCommand;
import com.lld.filestorage.command.ShareCommand;
import com.lld.filestorage.command.UploadCommand;
import com.lld.filestorage.domain.AccessType;
import com.lld.filestorage.domain.File;
import com.lld.filestorage.domain.FileMetadata;
import com.lld.filestorage.domain.FileSystemNode;
import com.lld.filestorage.domain.FileVersion;
import com.lld.filestorage.domain.Folder;
import com.lld.filestorage.domain.Permission;
import com.lld.filestorage.domain.User;
import com.lld.filestorage.exception.NodeNotFoundException;
import com.lld.filestorage.exception.PermissionDeniedException;
import com.lld.filestorage.observer.FileEventPublisher;
import com.lld.filestorage.storage.StorageStrategy;

import java.util.List;

/**
 * Core service — orchestrates commands and exposes a clean API.
 *
 * SRP  : Delegates each operation to a dedicated Command — no business logic lives here.
 * DIP  : Depends on StorageStrategy (interface), FileEventPublisher, and VersionIdGenerator.
 * OCP  : New operations are added as new Command classes; this service gains one new method.
 *
 * This class is NOT a God object — it is an orchestrator. Each operation is a single
 * line that constructs and executes a Command.
 */
public final class FileStorageService {

    private final Folder             root;
    private final StorageStrategy    storage;
    private final FileEventPublisher publisher;
    private final VersionIdGenerator versionIdGen;

    public FileStorageService(final Folder root,
                              final StorageStrategy storage,
                              final FileEventPublisher publisher,
                              final VersionIdGenerator versionIdGen) {
        if (root         == null) throw new IllegalArgumentException("root required");
        if (storage      == null) throw new IllegalArgumentException("storage required");
        if (publisher    == null) throw new IllegalArgumentException("publisher required");
        if (versionIdGen == null) throw new IllegalArgumentException("versionIdGen required");
        this.root         = root;
        this.storage      = storage;
        this.publisher    = publisher;
        this.versionIdGen = versionIdGen;
    }

    // ── Write operations ─────────────────────────────────────────────────────

    /**
     * Uploads a file. Creates a new version if the file already exists.
     * @return the FileVersion that was created.
     */
    public FileVersion upload(final FileMetadata metadata) {
        return (FileVersion) execute(
                new UploadCommand(metadata, root, storage, publisher, versionIdGen));
    }

    /**
     * Creates a new folder at the given path.
     * The parent path must already exist.
     *
     * @param parentPath  path of the parent folder (e.g. "/docs")
     * @param folderName  name of the new folder
     * @param owner       user creating the folder
     * @return the created Folder
     */
    public Folder createFolder(final String parentPath, final String folderName, final User owner) {
        final Folder parent    = FolderNavigator.navigate(root, parentPath);
        final Folder newFolder = new Folder(folderName, owner);
        parent.addChild(newFolder);
        return newFolder;
    }

    /**
     * Deletes a file or folder (cascades to children).
     */
    public void delete(final User requester, final String folderPath, final String targetName) {
        execute(new DeleteCommand(requester, folderPath, targetName, root, storage, publisher));
    }

    /**
     * Shares a node (file or folder) with a user at the given access level.
     * @return the Permission that was granted.
     */
    public Permission share(final User sharer,
                            final String folderPath,
                            final String nodeName,
                            final User recipient,
                            final AccessType accessType) {
        return (Permission) execute(
                new ShareCommand(sharer, folderPath, nodeName, recipient, accessType, root, publisher));
    }

    // ── Read operations ──────────────────────────────────────────────────────

    /**
     * Downloads the latest version of a file.
     * @return raw content bytes.
     */
    public byte[] download(final User requester, final String folderPath, final String fileName) {
        return (byte[]) execute(
                new DownloadCommand(requester, folderPath, fileName, root, storage, publisher));
    }

    /**
     * Lists children of a folder. Requester needs READ access.
     */
    public List<FileSystemNode> list(final User requester, final String folderPath) {
        final Folder folder = FolderNavigator.navigate(root, folderPath);
        if (!folder.hasAccess(requester, AccessType.READ)) {
            throw new PermissionDeniedException(
                    "User '" + requester.getUserId() + "' lacks READ access on '" + folderPath + "'");
        }
        return folder.getChildren();
    }

    /**
     * Returns all versions of a file. Requester needs READ access.
     */
    public List<FileVersion> listVersions(final User requester,
                                          final String folderPath,
                                          final String fileName) {
        final Folder folder = FolderNavigator.navigate(root, folderPath);
        final FileSystemNode node = folder.findChild(fileName)
                .orElseThrow(() -> new NodeNotFoundException(
                        "File '" + fileName + "' not found in '" + folderPath + "'"));

        if (node.isFolder()) {
            throw new IllegalArgumentException("'" + fileName + "' is a folder, not a file.");
        }
        if (!node.hasAccess(requester, AccessType.READ)) {
            throw new PermissionDeniedException(
                    "User '" + requester.getUserId() + "' lacks READ access on '" + fileName + "'");
        }
        return ((File) node).getAllVersions();
    }

    // ── Command invoker ──────────────────────────────────────────────────────

    /**
     * Command Pattern — invoker method.
     * Centralised execution point; ideal for adding cross-cutting concerns
     * (logging, metrics, retry) without modifying individual commands.
     */
    private Object execute(final FileCommand command) {
        return command.execute();
    }
}
