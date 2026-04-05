package com.lld.filestorage;

import com.lld.filestorage.domain.AccessType;
import com.lld.filestorage.domain.FileMetadata;
import com.lld.filestorage.domain.FileSystemNode;
import com.lld.filestorage.domain.FileVersion;
import com.lld.filestorage.domain.Permission;
import com.lld.filestorage.domain.User;
import com.lld.filestorage.exception.PermissionDeniedException;
import com.lld.filestorage.facade.FileStorageFacade;
import com.lld.filestorage.storage.InMemoryStorageStrategy;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * End-to-end demonstration of the File Storage System.
 *
 * Covers:
 *   1. Folder creation
 *   2. File upload (first version)
 *   3. File versioning (re-upload same file)
 *   4. File download
 *   5. Sharing with another user
 *   6. Permission enforcement (access denied scenario)
 *   7. Folder listing
 *   8. File deletion
 */
public final class Main {

    public static void main(final String[] args) {

        // ── Bootstrap ────────────────────────────────────────────────────────
        // Facade wires the full dependency graph; swap InMemoryStorageStrategy
        // for LocalStorageStrategy or CloudStorageStrategy without code change.
        final FileStorageFacade fs = new FileStorageFacade(new InMemoryStorageStrategy());

        final User alice = new User("alice", "Alice");
        final User bob   = new User("bob",   "Bob");

        section("1. CREATE FOLDERS");
        fs.createFolder("/", "documents", alice);
        fs.createFolder("/documents", "work", alice);
        System.out.println("  Created: /documents/work");

        // ── Upload ───────────────────────────────────────────────────────────
        section("2. UPLOAD FILE (v1)");
        final FileMetadata v1Meta = FileMetadata.builder()
                .fileName("report.pdf")
                .mimeType("application/pdf")
                .targetFolderPath("/documents/work")
                .uploader(alice)
                .content("Annual Report - Draft 1".getBytes(StandardCharsets.UTF_8))
                .build();

        final FileVersion v1 = fs.upload(v1Meta);
        System.out.printf("  Uploaded version: %s  size=%d bytes%n",
                v1.getVersionId(), v1.getSizeBytes());

        // ── Versioning ───────────────────────────────────────────────────────
        section("3. RE-UPLOAD SAME FILE → creates v2");
        final FileMetadata v2Meta = FileMetadata.builder()
                .fileName("report.pdf")
                .mimeType("application/pdf")
                .targetFolderPath("/documents/work")
                .uploader(alice)
                .content("Annual Report - Final".getBytes(StandardCharsets.UTF_8))
                .build();

        final FileVersion v2 = fs.upload(v2Meta);
        System.out.printf("  Uploaded version: %s  size=%d bytes%n",
                v2.getVersionId(), v2.getSizeBytes());

        final List<FileVersion> versions = fs.listVersions(alice, "/documents/work", "report.pdf");
        System.out.printf("  Total versions: %d%n", versions.size());
        versions.forEach(v -> System.out.printf("    → %s at %s%n", v.getVersionId(), v.getTimestamp()));

        // ── Download ─────────────────────────────────────────────────────────
        section("4. DOWNLOAD LATEST VERSION");
        final byte[] downloaded = fs.download(alice, "/documents/work", "report.pdf");
        System.out.printf("  Downloaded: \"%s\"%n",
                new String(downloaded, StandardCharsets.UTF_8));

        // ── Sharing ──────────────────────────────────────────────────────────
        section("5. SHARE FILE WITH BOB (READ access)");
        final Permission granted = fs.share(alice, "/documents/work", "report.pdf",
                bob, AccessType.READ);
        System.out.printf("  Granted: %s%n", granted);

        // Bob can now download
        final byte[] bobDownload = fs.download(bob, "/documents/work", "report.pdf");
        System.out.printf("  Bob read: \"%s\"%n",
                new String(bobDownload, StandardCharsets.UTF_8));

        // ── Permission enforcement ───────────────────────────────────────────
        section("6. PERMISSION ENFORCEMENT — Bob tries to delete (should fail)");
        try {
            fs.delete(bob, "/documents/work", "report.pdf");
            System.out.println("  ERROR: delete should have been rejected!");
        } catch (PermissionDeniedException e) {
            System.out.println("  Correctly rejected: " + e.getMessage());
        }

        // ── Listing ──────────────────────────────────────────────────────────
        section("7. LIST FOLDER CONTENTS");
        fs.createFolder("/documents", "personal", alice);
        final FileMetadata noteMeta = FileMetadata.builder()
                .fileName("notes.txt")
                .targetFolderPath("/documents/work")
                .uploader(alice)
                .content("Meeting notes".getBytes(StandardCharsets.UTF_8))
                .build();
        fs.upload(noteMeta);

        final List<FileSystemNode> workContents = fs.list(alice, "/documents/work");
        System.out.printf("  /documents/work (%d items):%n", workContents.size());
        for (FileSystemNode node : workContents) {
            System.out.printf("    [%s] %s%n", node.isFolder() ? "DIR " : "FILE", node.getName());
        }

        // ── Delete ───────────────────────────────────────────────────────────
        section("8. DELETE FILE");
        fs.delete(alice, "/documents/work", "notes.txt");
        final List<FileSystemNode> afterDelete = fs.list(alice, "/documents/work");
        System.out.printf("  /documents/work after delete (%d item):%n", afterDelete.size());
        afterDelete.forEach(n -> System.out.printf("    [%s] %s%n",
                n.isFolder() ? "DIR " : "FILE", n.getName()));

        section("DEMO COMPLETE");
    }

    private static void section(final String title) {
        System.out.println();
        System.out.println("══════════════════════════════════════════");
        System.out.println("  " + title);
        System.out.println("══════════════════════════════════════════");
    }
}
